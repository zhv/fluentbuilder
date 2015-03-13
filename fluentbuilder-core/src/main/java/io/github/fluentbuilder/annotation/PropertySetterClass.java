package io.github.fluentbuilder.annotation;

import io.github.fluentbuilder.api.PropertySetter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface PropertySetterClass {
    Class<? extends PropertySetter> value();
}
