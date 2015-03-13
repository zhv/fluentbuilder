package io.github.fluentbuilder;

public interface BuilderFactoryFacade {

    public <T, B extends RootBuilder<T>> B createRootBuilder(Class<B> builderClass);

    public <T, B extends RootBuilder<T>> B createRootBuilder(Class<B> builderClass, T instance);

}
