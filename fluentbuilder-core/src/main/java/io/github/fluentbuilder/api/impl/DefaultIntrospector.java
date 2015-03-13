package io.github.fluentbuilder.api.impl;

import io.github.fluentbuilder.RootBuilder;
import io.github.fluentbuilder.annotation.TargetClass;
import io.github.fluentbuilder.api.BuilderIntrospector;
import io.github.fluentbuilder.internal.BuilderMethodType;
import io.github.fluentbuilder.internal.BuilderModel;

import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultIntrospector implements BuilderIntrospector {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultIntrospector.class);

    private static final String TARGET_CLASS_ANNOTATION_MESSAGE = "TargetClass annotation not found for builderClass";
    private static final String INCOMPATIBLE_TARGET_CLASS_MESSAGE = "TargetClass incompatible instance object";

    public BuilderModel introspect(Class<? extends RootBuilder<?>> builderClass, Object target) {
        return introspect(null, builderClass, null);
    }

    @SuppressWarnings("unchecked")
    protected BuilderModel introspect(BuilderModel parentModel, Class<? extends RootBuilder<?>> builderClass,
            Object target) {
        TargetClass targetClassAnnotation = builderClass.getAnnotation(TargetClass.class);
        if (targetClassAnnotation == null) {
            throw new IllegalArgumentException(TARGET_CLASS_ANNOTATION_MESSAGE);
        }

        Class<?> targetClass = targetClassAnnotation.value();
        if (target != null && !targetClass.isInstance(target)) {
            throw new IllegalArgumentException(INCOMPATIBLE_TARGET_CLASS_MESSAGE);
        }

        LOG.debug("targetClass={}", targetClass.getName());

        BuilderModel builderModel = new BuilderModel(parentModel, builderClass, targetClass);

        for (Method method : builderClass.getMethods()) {
            BuilderMethodType methodType = BuilderMethodType.fromMethod(builderClass, method);
            LOG.debug("methodType={}, method={}", methodType, method);

            if (methodType == BuilderMethodType.NONE) {
                continue;
            }

            if (methodType == BuilderMethodType.NESTED_BUILDER_SWITCH) {
                BuilderModel nestedBuilderModel = introspect(builderModel,
                        (Class<? extends RootBuilder<?>>) method.getReturnType(), null);
                builderModel.getNestedModels().put(method, nestedBuilderModel);
            }

            builderModel.getMethods().put(method, methodType);
        }
        return builderModel;
    }
}
