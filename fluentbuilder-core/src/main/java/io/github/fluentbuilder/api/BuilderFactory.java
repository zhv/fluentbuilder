package io.github.fluentbuilder.api;

import io.github.fluentbuilder.internal.BuilderModel;
import io.github.fluentbuilder.internal.BuilderStackFrame;
import io.github.fluentbuilder.internal.ServiceCartrige;

import java.util.Stack;


public interface BuilderFactory {

    Object newBuilder(BuilderModel model, ServiceCartrige serviceCartrige, Stack<BuilderStackFrame> stack);

}
