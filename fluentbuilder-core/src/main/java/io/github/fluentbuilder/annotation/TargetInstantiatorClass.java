package io.github.fluentbuilder.annotation;

import io.github.fluentbuilder.api.TargetInstantiator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface TargetInstantiatorClass {
    Class<? extends TargetInstantiator> value();
}
