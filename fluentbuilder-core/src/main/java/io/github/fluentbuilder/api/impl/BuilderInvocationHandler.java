package io.github.fluentbuilder.api.impl;

import io.github.fluentbuilder.api.BuilderFactory;
import io.github.fluentbuilder.internal.BuilderMethodType;
import io.github.fluentbuilder.internal.BuilderModel;
import io.github.fluentbuilder.internal.BuilderStackFrame;
import io.github.fluentbuilder.internal.ServiceCartrige;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Stack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BuilderInvocationHandler implements InvocationHandler {

    private static final Logger LOG = LoggerFactory.getLogger(BuilderInvocationHandler.class);

    private BuilderModel model;
    private BuilderFactory builderFactory;
    private ServiceCartrige serviceCartrige;
    private Stack<BuilderStackFrame> stack;

    private Object target;

    public BuilderInvocationHandler(BuilderModel model, BuilderFactory builderImplementor,
            ServiceCartrige serviceCartrige, Stack<BuilderStackFrame> stack) {
        this.model = model;
        this.builderFactory = builderImplementor;
        this.serviceCartrige = serviceCartrige;
        this.stack = stack;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        LOG.debug("invoke method, method={}", method);

        BuilderMethodType methodType = model.getMethods().get(method);

        if (methodType == BuilderMethodType.BUILD_METHOD) {
            return invokeBuild(proxy, method, args);
        } else if (methodType == BuilderMethodType.SETTER) {
            return invokeSetter(proxy, method, args);
        } else if (methodType == BuilderMethodType.NESTED_BUILDER_SWITCH) {
            return invokeNestedBuilderSwitch(proxy, method, args);
        } else if (methodType == BuilderMethodType.PARENT_BUILDER_SWITCH) {
            return invokeParentBuilderSwitch(proxy, method, args);
        }

        throw new UnsupportedOperationException("Method " + method.getName() + " is not recognized");
    }

    protected Object invokeBuild(Object proxy, Method method, Object[] args) {
        return getTarget();
    }

    protected Object invokeSetter(Object proxy, Method method, Object[] args) {
        Object targetLocal = getTarget();
        serviceCartrige.getPropertySetter().setProperty(method, model, targetLocal, args);
        return proxy;
    }

    @SuppressWarnings("unchecked")
    protected Object invokeNestedBuilderSwitch(Object proxy, Method method, Object[] args) {
        BuilderModel nestedModel = model.getNestedModels().get(method);
        if (nestedModel == null) {
            throw new IllegalArgumentException("Unnable to find nested BuilderModel");
        }

        Object targetLocal = getTarget();
        BuilderStackFrame newFrame = new BuilderStackFrame(model, targetLocal, proxy, method);
        Stack<BuilderStackFrame> newStack = (Stack<BuilderStackFrame>) stack.clone();
        newStack.push(newFrame);

        return builderFactory.newBuilder(nestedModel, serviceCartrige, newStack);
    }

    protected Object invokeParentBuilderSwitch(Object proxy, Method method, Object[] args) {
        BuilderStackFrame frame = stack.pop();

        Object parentBuilder = frame.getBuilder();
        if (parentBuilder == null) {
            throw new IllegalArgumentException("Unnable to find parent Builder");
        }

        Method builderSwitchMethod = frame.getNestedSwitchMethod();
        BuilderModel parentModel = frame.getModel();
        Object parentTarget = frame.getTarget();
        Object targetLocal = getTarget();

        serviceCartrige.getPropertySetter().addNestedObject(builderSwitchMethod, parentModel, parentTarget, model, targetLocal);

        return parentBuilder;
    }

    protected Object getTarget() {
        if (target == null) {
            target = serviceCartrige.getModelInstantiator().newTarget(model);
        }
        return target;
    }
}