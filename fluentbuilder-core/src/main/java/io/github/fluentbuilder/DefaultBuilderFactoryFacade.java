package io.github.fluentbuilder;

import io.github.fluentbuilder.api.BuilderFactory;
import io.github.fluentbuilder.api.BuilderIntrospector;
import io.github.fluentbuilder.internal.BuilderModel;
import io.github.fluentbuilder.internal.BuilderServiceLoader;
import io.github.fluentbuilder.internal.BuilderStackFrame;
import io.github.fluentbuilder.internal.ServiceCartrige;

import java.util.Stack;

public class DefaultBuilderFactoryFacade implements BuilderFactoryFacade {

    private static final String BUILDER_CLASS_NULL_ERROR_MESSAGE = "Argument builderClass can't be null";
    private static final String INSTANCE_NULL_ERROR_MESSAGE = "Argument instance can't be null";

    public <T, B extends RootBuilder<T>> B createRootBuilder(Class<B> builderClass) {
        if (builderClass == null) {
            throw new IllegalArgumentException(BUILDER_CLASS_NULL_ERROR_MESSAGE);
        }
        return createRootBuilderInt(builderClass, null);
    }

    public <T, B extends RootBuilder<T>> B createRootBuilder(Class<B> builderClass, T instance) {
        if (builderClass == null) {
            throw new IllegalArgumentException(BUILDER_CLASS_NULL_ERROR_MESSAGE);
        }
        if (instance == null) {
            throw new IllegalArgumentException(INSTANCE_NULL_ERROR_MESSAGE);
        }
        return createRootBuilderInt(builderClass, instance);
    }

    @SuppressWarnings("unchecked")
    protected <T, B extends RootBuilder<T>> B createRootBuilderInt(Class<B> builderClass, T instance) {
        BuilderServiceLoader sl = new BuilderServiceLoader();

        BuilderIntrospector introspector = sl.createService(BuilderIntrospector.class, sl.getIntrospectorClass(builderClass));
        if (introspector == null) {
            throw new IllegalArgumentException("Unnable to find BuilderIntrospector");
        }

        BuilderFactory factory = sl.createService(BuilderFactory.class, sl.getInstantiatorClass(builderClass));
        if (factory == null) {
            throw new IllegalArgumentException("Unnable to find BuilderFactory");
        }

        ServiceCartrige serviceCartrige = sl.createServiceCartrige(builderClass);
        BuilderModel model = introspector.introspect(builderClass, instance);
        Object builderInstance = factory.newBuilder(model, serviceCartrige, new Stack<BuilderStackFrame>());
        return (B) builderInstance;
    }
}
