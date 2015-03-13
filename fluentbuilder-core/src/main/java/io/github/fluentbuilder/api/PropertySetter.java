package io.github.fluentbuilder.api;

import io.github.fluentbuilder.internal.BuilderModel;

import java.lang.reflect.Method;

public interface PropertySetter {

    void setProperty(Method builderMethod, BuilderModel model, Object target, Object[] values);

    void addNestedObject(Method builderMethod, BuilderModel model, Object target, BuilderModel nestedModel, Object nestedTarget);

}
