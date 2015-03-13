package io.github.fluentbuilder.api;

import io.github.fluentbuilder.RootBuilder;
import io.github.fluentbuilder.internal.BuilderModel;

public interface BuilderIntrospector {

    BuilderModel introspect(Class<? extends RootBuilder<?>> builderClass, Object target);

}
