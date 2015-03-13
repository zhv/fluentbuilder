package io.github.fluentbuilder.internal;

import io.github.fluentbuilder.api.PropertySetter;
import io.github.fluentbuilder.api.TargetInstantiator;

public class ServiceCartrige {

    private TargetInstantiator targetInstantiator;
    private PropertySetter propertySetter;

    public ServiceCartrige(TargetInstantiator targetInstantiator, PropertySetter propertySetter) {
        this.targetInstantiator = targetInstantiator;
        this.propertySetter = propertySetter;
    }

    public TargetInstantiator getModelInstantiator() {
        return targetInstantiator;
    }

    public PropertySetter getPropertySetter() {
        return propertySetter;
    }
}
