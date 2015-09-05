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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.Iterator;
import lombok.extern.slf4j.Slf4j;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author zhanhb
 */
@Slf4j
public class CustomInvocationHandlerTest {

    @SuppressWarnings("unchecked")
    private static <T> T newProxy(Class<T> cl, Object... parents) {
        CustomInvocationHandler instance = new CustomInvocationHandler(parents);
        return (T) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                new Class<?>[]{cl}, instance);
    }

    private Object orign;
    private C proxy;

    @Before
    public void setUp() {
        orign = new Object() {
            public int g() {
                return 1;
            }
        };
        proxy = newProxy(C.class, orign);
    }

    /**
     * Test of invoke method, of class CustomInvocationHandler.
     *
     * @throws java.lang.Throwable
     */
    @Test
    public void testProxy() throws Throwable {
        log.info("invoke");
        assertEquals("proxy should equals itsself", proxy, proxy);
        assertNotEquals(orign, proxy);
        assertEquals(System.identityHashCode(proxy), proxy.hashCode());

        log.info(proxy.toString());
    }

    @Test(expected = AbstractMethodError.class)
    public void testNoSuchMethodException() {
        proxy.f();
    }

    @Test
    public void testOk() {
        assertEquals(1, proxy.g());
    }

    // only JDK 8+
    @Test
    public void testDefaultMethod() {
        try {
            newProxy(Iterator.class).remove();
            fail("should throw an unsupported operation exception");
        } catch (UnsupportedOperationException ex) {
            // ok
        }
    }

    @Test
    public void testDefaultMethodImplementation() {
        Iterator<?> it = newProxy(Iterator.class, new Object() {
            public void remove() {
                throw new IllegalStateException();
            }
        });

        try {
            it.remove();
            fail("should throw an IllegalStateException");
        } catch (IllegalStateException ex) {
            // ok
        }
    }

    @Test
    public void testCloneable() {
        assertSame(Proxy.getInvocationHandler(proxy.clone()), Proxy.getInvocationHandler(proxy));
    }

    @Test(expected = IllegalStateException.class)
    public void testThrowException() {
        orign = new Object() {
            public void f() {
                throw new IllegalStateException();
            }
        };
        newProxy(C.class, orign).f();
    }

    @Test(expected = UndeclaredThrowableException.class)
    public void testUndeclaredThrowableException() {
        orign = new Object() {
            public void f() throws InvocationTargetException {
                throw new InvocationTargetException(new IllegalStateException());
            }
        };
        newProxy(C.class, orign).f();
    }

    @Test(expected = InvocationTargetException.class)
    public void testInvocationTargetException() throws Exception {
        newProxy(C.class, new Object() {
            public void h() throws InvocationTargetException {
                throw new InvocationTargetException(new IllegalStateException());
            }
        }).h();
    }

    private static interface C extends Cloneable {

        Object clone();

        void f();

        int g();

        void h() throws Exception;
    }

}
