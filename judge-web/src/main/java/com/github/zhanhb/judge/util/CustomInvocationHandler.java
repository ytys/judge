/*
 * Copyright 2015 zhanhb.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.zhanhb.judge.util;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import org.springframework.asm.Type;

/**
 *
 * @author zhanhb
 */
class CustomInvocationHandler implements InvocationHandler {

    private static final MethodHandles.Lookup trusted = getTrusted();

    private static MethodHandles.Lookup getTrusted() {
        try {
            return getTrusted1();
        } catch (Throwable ex) {
            try {
                return getTrusted2();
            } catch (Throwable ex1) {
                throw new ExceptionInInitializerError(ex);
            }
        }
    }

    private static MethodHandles.Lookup getTrusted1() throws Throwable {
        Field field = MethodHandles.Lookup.class.getDeclaredField("IMPL_LOOKUP");
        field.setAccessible(true);
        return (MethodHandles.Lookup) field.get(null);
    }

    private static MethodHandles.Lookup getTrusted2() throws Throwable {
        Constructor<MethodHandles.Lookup> constructor = MethodHandles.Lookup.class.getDeclaredConstructor(Class.class, int.class);
        if (!constructor.isAccessible()) {
            constructor.setAccessible(true);
        }
        return constructor.newInstance(Object.class, -1);
    }

    static boolean isDefaultMethod(Method method) {
        return ((method.getModifiers() & (Modifier.ABSTRACT | Modifier.PUBLIC | Modifier.STATIC)) == Modifier.PUBLIC)
                && method.getDeclaringClass().isInterface();
    }

    private final Object[] parents;

    CustomInvocationHandler(Object... parents) {
        Object[] clone = parents.clone();
        for (Object parent : clone) {
            if (parent == null) {
                throw new NullPointerException();
            }
        }
        this.parents = clone;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (proxy == null) {
            throw new NullPointerException();
        }
        if (!Proxy.isProxyClass(proxy.getClass())) {
            throw new IllegalArgumentException("not a proxy instance");
        }

        String methodName = method.getName();
        if (method.getDeclaringClass() == Object.class
                || method.getParameterCount() == 0
                && ("clone".equals(methodName) || "finalize".equals(methodName))) {
            return invoke(Object.class, method, proxy, args);
        }

        for (int i = 0, len = parents.length; i < len; ++i) {
            Object parent = parents[i];
            Method m;
            try {
                m = parent.getClass().getMethod(method.getName(), method.getParameterTypes());
            } catch (NoSuchMethodException ex) {
                continue;
            }
            try {
                return m.invoke(parent, args);
            } catch (InvocationTargetException ex) {
                throw ex.getTargetException();
            }
        }

        if (isDefaultMethod(method)) {
            return invoke(method, proxy, args);
        }

        throw new AbstractMethodError(proxy.getClass().getName() + '.' + methodName + Type.getMethodDescriptor(method));
    }

    private Object invoke(Method method, Object proxy, Object[] args) throws Throwable {
        return trusted.unreflectSpecial(method, method.getDeclaringClass()).bindTo(proxy)
                .invokeWithArguments(args);
    }

    private Object invoke(Class<?> declaringClass, Method method, Object proxy, Object[] args)
            throws Throwable {
        return trusted.findSpecial(declaringClass, method.getName(),
                MethodType.methodType(method.getReturnType(), method.getParameterTypes()),
                declaringClass).bindTo(proxy).invokeWithArguments(args);
    }

}
