package io.github.fluentbuilder.api;

import io.github.fluentbuilder.internal.BuilderModel;

public interface TargetInstantiator {

    Object newTarget(BuilderModel model);

}
