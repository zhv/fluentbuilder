package io.github.fluentbuilder.api.impl;

import io.github.fluentbuilder.api.PropertySetter;
import io.github.fluentbuilder.internal.BuilderModel;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class BeanPropertySetter implements PropertySetter {

    private static final Map<Class<?>, Class<?>> OBJECT_TYPE_TO_PRIMITIVE;

    static {
        Map<Class<?>, Class<?>> m = new HashMap<Class<?>, Class<?>>();
        m.put(Boolean.class, Boolean.TYPE);
        m.put(Byte.class, Byte.TYPE);
        m.put(Character.class, Character.TYPE);
        m.put(Short.class, Short.TYPE);
        m.put(Integer.class, Integer.TYPE);
        m.put(Long.class, Long.TYPE);
        m.put(Float.class, Float.TYPE);
        m.put(Double.class, Double.TYPE);
        OBJECT_TYPE_TO_PRIMITIVE = Collections.unmodifiableMap(m);
    }

    public void setProperty(Method builderMethod, BuilderModel model, Object target, Object[] values) {
        Class<?> targetClass = model.getTargetClass();
        BeanInfo beanInfo = null;

        try {
            beanInfo = Introspector.getBeanInfo(targetClass);
        } catch (IntrospectionException e) {
            throw new IllegalStateException("Unnable to get BeanInfo instance", e);
        }

        String propertyName = model.getPropertyName(builderMethod);
        Method writeMethod = findWriteMethod(beanInfo, propertyName, getClasses(values));
        if (writeMethod == null) {
            throw new IllegalArgumentException("Unnable to find suitable setter method");
        }

        invokeMethod(writeMethod, target, values);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void addNestedObject(Method builderMethod, BuilderModel model, Object target, BuilderModel nestedModel,
            Object nestedTarget) {
        Class<?> targetClass = model.getTargetClass();
        BeanInfo beanInfo = null;

        try {
            beanInfo = Introspector.getBeanInfo(targetClass);
        } catch (IntrospectionException e) {
            throw new IllegalStateException("Unnable to get BeanInfo instance", e);
        }

        String propertyName = model.getPropertyName(builderMethod) + "s";

        Method readMethod = findReadMethod(beanInfo, propertyName);
        if (readMethod == null) {
            throw new IllegalArgumentException("Unnable to find suitable getter method");
        }

        Collection currentCollection = null;

        Object obj = invokeMethod(readMethod, target);
        if (obj != null) {
            currentCollection = (Collection) obj;
        }

        Method writeMethod = findWriteMethod(beanInfo, propertyName, new Class<?>[] { Collection.class });
        if (writeMethod != null) {
            Collection changedCollection = new ArrayList();
            if (currentCollection != null) {
                changedCollection.addAll(currentCollection);
            }
            changedCollection.add(nestedTarget);
            invokeMethod(writeMethod, target, changedCollection);
        } else {
            if (currentCollection == null) {
                throw new IllegalArgumentException("Property without setter should be pre-initialized");
            }
            currentCollection.add(nestedTarget);
        }
    }

    protected Method findWriteMethod(BeanInfo beanInfo, String propertyName, Class<?>[] argClasses) {
        Method writeMethod = null;
        for (PropertyDescriptor pd : beanInfo.getPropertyDescriptors()) {
            if (propertyName.equals(pd.getName())) {
                Method m = pd.getWriteMethod();
                if (m != null && isParametersCompatible(m.getParameterTypes(), argClasses)) {
                    writeMethod = m;
                    break;
                }
            }
        }
        return writeMethod;
    }

    protected Method findReadMethod(BeanInfo beanInfo, String propertyName) {
        Method readMethod = null;
        for (PropertyDescriptor pd : beanInfo.getPropertyDescriptors()) {
            if (propertyName.equals(pd.getName())) {
                Method m = pd.getReadMethod();
                if (m != null) {
                    readMethod = m;
                    break;
                }
            }
        }
        return readMethod;
    }

    protected boolean isParametersCompatible(Class<?>[] cls, Class<?>[] cls2) {
        boolean clsEmpty = isEmptyArray(cls);
        boolean cls2Empty = isEmptyArray(cls2);

        if (clsEmpty && cls2Empty) {
            return true;
        } else if (clsEmpty || cls2Empty) {
            return false;
        }

        int count = cls.length;
        if (count != cls2.length) {
            return false;
        }

        for (int i = 0; i < count; i++) {
            Class<?> cl = cls[i];
            Class<?> cl2 = cls2[i];
            if (cl2 == null) {
                // null argument passed to the builder implementation. type checking is not required
                continue;
            }

            if (cl2.isAssignableFrom(cl)) {
                continue;
            }

            Class<?> primitiveType = OBJECT_TYPE_TO_PRIMITIVE.get(cl2);
            if (primitiveType == null || !primitiveType.isAssignableFrom(cl)) {
                return false;
            }
        }

        return true;
    }

    public static boolean isEmptyArray(Object[] obj) {
        return obj == null || obj.length == 0;
    }

    protected Class<?>[] getClasses(Object... values) {
        if (values == null) {
            return new Class<?>[] {};
        }
        int count = values.length;
        Class<?>[] result = new Class<?>[count];
        for (int i = 0; i < count; i++) {
            Object obj = values[i];
            result[i] = (obj != null) ? obj.getClass() : null;
        }
        return result;
    }

    protected Object invokeMethod(Method method, Object target, Object... arguments) {
        try {
            return method.invoke(target, arguments);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("Unnable to invoke setter", e);
        } catch (IllegalArgumentException e) {
            throw new IllegalStateException("Unnable to invoke setter", e);
        } catch (InvocationTargetException e) {
            throw new IllegalStateException("Unnable to invoke setter", e);
        }
    }
}
