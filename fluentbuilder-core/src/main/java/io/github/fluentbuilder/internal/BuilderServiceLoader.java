package io.github.fluentbuilder.internal;

import io.github.fluentbuilder.RootBuilder;
import io.github.fluentbuilder.annotation.FactoryClass;
import io.github.fluentbuilder.annotation.IntrospectorClass;
import io.github.fluentbuilder.annotation.PropertySetterClass;
import io.github.fluentbuilder.annotation.TargetInstantiatorClass;
import io.github.fluentbuilder.api.BuilderFactory;
import io.github.fluentbuilder.api.BuilderIntrospector;
import io.github.fluentbuilder.api.PropertySetter;
import io.github.fluentbuilder.api.TargetInstantiator;

import java.lang.annotation.Annotation;

public class BuilderServiceLoader {

    public Class<? extends BuilderIntrospector> getIntrospectorClass(Class<? extends RootBuilder<?>> builderClass) {
        IntrospectorClass annotation = getAnnotationRecusively(builderClass, IntrospectorClass.class);
        return annotation.value();
    }

    public Class<? extends PropertySetter> getPropertySetterClass(Class<? extends RootBuilder<?>> builderClass) {
        PropertySetterClass annotation = getAnnotationRecusively(builderClass, PropertySetterClass.class);
        return annotation.value();
    }

    public Class<? extends BuilderFactory> getInstantiatorClass(Class<? extends RootBuilder<?>> builderClass) {
        FactoryClass annotation = getAnnotationRecusively(builderClass, FactoryClass.class);
        return annotation.value();
    }

    public Class<? extends TargetInstantiator> getTargetInstantiatorClass(Class<? extends RootBuilder<?>> builderClass) {
        TargetInstantiatorClass annotation = getAnnotationRecusively(builderClass, TargetInstantiatorClass.class);
        return annotation.value();
    }

    public <T> T createService(Class<T> serviceClass, Class<? extends T> definedClass) {
        try {
            Object instance = definedClass.newInstance();
            return serviceClass.cast(instance);
        } catch (InstantiationException e) {
            throw new IllegalStateException("Unnable to instantiate service implementation class", e);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("Unnable to instantiate service implementation class", e);
        }
    }

    public ServiceCartrige createServiceCartrige(Class<? extends RootBuilder<?>> builderClass) {
        TargetInstantiator targetInstantiator = createService(TargetInstantiator.class,
                getTargetInstantiatorClass(builderClass));
        if (targetInstantiator == null) {
            throw new IllegalArgumentException("Unnable to find TargetInstantiator");
        }

        PropertySetter propertySetter = createService(PropertySetter.class, getPropertySetterClass(builderClass));
        if (propertySetter == null) {
            throw new IllegalArgumentException("Unnable to find PropertySetter");
        }

        return new ServiceCartrige(targetInstantiator, propertySetter);
    }

    protected <A extends Annotation> A getAnnotationRecusively(Class<?> cl, Class<A> annotationClass) {
        A annotation = cl.getAnnotation(annotationClass);
        if (annotation != null) {
            return annotation;
        }

        Class<?> superclass = cl.getSuperclass();
        if (superclass != null) {
            annotation = getAnnotationRecusively(superclass, annotationClass);
            if (annotation != null) {
                return annotation;
            }
        }

        for (Class<?> interfaceClass : cl.getInterfaces()) {
            annotation = getAnnotationRecusively(interfaceClass, annotationClass);
            if (annotation != null) {
                return annotation;
            }
        }

        throw new IllegalArgumentException("Unnable to find specified Annotation, annotationClass="
                + annotationClass.getName());
    }
}
