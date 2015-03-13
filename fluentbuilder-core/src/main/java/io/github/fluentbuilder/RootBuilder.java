package io.github.fluentbuilder;

import io.github.fluentbuilder.annotation.FactoryClass;
import io.github.fluentbuilder.annotation.IntrospectorClass;
import io.github.fluentbuilder.annotation.PropertySetterClass;
import io.github.fluentbuilder.annotation.TargetInstantiatorClass;
import io.github.fluentbuilder.api.impl.BeanPropertySetter;
import io.github.fluentbuilder.api.impl.DefaultIntrospector;
import io.github.fluentbuilder.api.impl.ReflectionBuilderFactory;
import io.github.fluentbuilder.api.impl.SimpleTargetInstantiator;

@IntrospectorClass(DefaultIntrospector.class)
@FactoryClass(ReflectionBuilderFactory.class)
@TargetInstantiatorClass(SimpleTargetInstantiator.class)
@PropertySetterClass(BeanPropertySetter.class)
public interface RootBuilder<T> {

    public T build();

}
