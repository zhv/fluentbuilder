package io.github.fluentbuilder.api.impl;

import io.github.fluentbuilder.api.BuilderFactory;
import io.github.fluentbuilder.internal.BuilderModel;
import io.github.fluentbuilder.internal.BuilderStackFrame;
import io.github.fluentbuilder.internal.ServiceCartrige;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.Stack;

public class ReflectionBuilderFactory implements BuilderFactory {

    public Object newBuilder(BuilderModel model, ServiceCartrige serviceCartrige, Stack<BuilderStackFrame> stack) {
        ClassLoader classLoader = getClass().getClassLoader();
        InvocationHandler h = new BuilderInvocationHandler(model, this, serviceCartrige, stack);
        return Proxy.newProxyInstance(classLoader, new Class<?>[] { model.getBuilderClass() }, h);
    }
}
