package io.github.fluentbuilder.internal;

import io.github.fluentbuilder.NestedBuilder;
import io.github.fluentbuilder.RootBuilder;

import java.lang.reflect.Method;

public enum BuilderMethodType {
    BUILD_METHOD,
    SETTER,
    NESTED_BUILDER_SWITCH,
    PARENT_BUILDER_SWITCH,
    NONE;

    public static BuilderMethodType fromMethod(Class<? extends RootBuilder<?>> builderClass, Method builderMethod) {
        String name = builderMethod.getName();

        if ("build".equals(name)) {
            return BUILD_METHOD;
        }

        if (name.startsWith("set") && name.length() > 3 && builderMethod.getParameterTypes().length > 0) {
            return SETTER;
        }

        Class<?> returnType = builderMethod.getReturnType();
        if (name.startsWith("add") && name.length() > 3 && NestedBuilder.class.isAssignableFrom(returnType)) {
            return NESTED_BUILDER_SWITCH;
        }

        if ("back".equals(name) && NestedBuilder.class.isAssignableFrom(builderClass)) {
            return PARENT_BUILDER_SWITCH;
        }

        return NONE;
    }
}