package io.github.fluentbuilder.internal;

import java.lang.reflect.Method;

public class BuilderStackFrame {

    private BuilderModel model;
    private Object target;

    private Object builder;
    private Method nestedSwitchMethod;

    public BuilderStackFrame(BuilderModel model, Object target, Object builder, Method nestedSwitchMethod) {
        this.model = model;
        this.target = target;
        this.builder = builder;
        this.nestedSwitchMethod = nestedSwitchMethod;
    }

    public BuilderModel getModel() {
        return model;
    }

    public Object getTarget() {
        return target;
    }

    public Object getBuilder() {
        return builder;
    }

    public Method getNestedSwitchMethod() {
        return nestedSwitchMethod;
    }
}
