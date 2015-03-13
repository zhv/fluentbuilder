package io.github.fluentbuilder.api.impl;

import io.github.fluentbuilder.api.TargetInstantiator;
import io.github.fluentbuilder.internal.BuilderModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleTargetInstantiator implements TargetInstantiator {

    private static final Logger LOG = LoggerFactory.getLogger(SimpleTargetInstantiator.class);

    public Object newTarget(BuilderModel model) {
        Class<?> targetClass = model.getTargetClass();

        LOG.debug("Instantiating target, targetClass={}", targetClass);

        try {
            return targetClass.newInstance();
        } catch (InstantiationException e) {
            throw new IllegalStateException("Unnable to instantiate target", e);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("Unnable to instantiate target", e);
        }
    }
}
