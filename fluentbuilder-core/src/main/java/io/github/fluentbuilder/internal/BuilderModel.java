package io.github.fluentbuilder.internal;

import io.github.fluentbuilder.RootBuilder;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class BuilderModel
{
    private BuilderModel parentModel;

    private Class<?> targetClass;
    private Class<? extends RootBuilder<?>> builderClass;

    private Map<Method, BuilderMethodType> methods = new HashMap<Method, BuilderMethodType>();
    private Map<Method, BuilderModel> nestedModels = new HashMap<Method, BuilderModel>();

    public BuilderModel(BuilderModel parentModel, Class<? extends RootBuilder<?>> builderClass, Class<?> targetClass) {
        this.parentModel = parentModel;
        this.targetClass = targetClass;
        this.builderClass = builderClass;
    }

    public String getPropertyName(Method accessMethod) {
        String name = accessMethod.getName();
        return name.substring(3, 4).toLowerCase() + name.substring(4);
    }

    public Class<?> getTargetClass() {
        return targetClass;
    }

    public Class<? extends RootBuilder<?>> getBuilderClass() {
        return builderClass;
    }

    public Map<Method, BuilderMethodType> getMethods() {
        return methods;
    }

    public Map<Method, BuilderModel> getNestedModels() {
        return nestedModels;
    }

    public BuilderModel getParentModel() {
        return parentModel;
    }
}
