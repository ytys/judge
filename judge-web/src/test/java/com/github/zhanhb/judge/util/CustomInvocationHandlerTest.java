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

import java.lang.reflect.Proxy;
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

    private Object orign;
    private C proxy;

    @Before
    public void setUp() {
        orign = new Object();
        CustomInvocationHandler instance = new CustomInvocationHandler(orign);
        proxy = (C) Proxy.newProxyInstance(C.class.getClassLoader(),
                new Class<?>[]{C.class}, instance);
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

    // only JDK 8+
    @Test
    public void testDefaultMethod() {
        Iterator<?> it = (Iterator) Proxy.newProxyInstance(
                CustomInvocationHandlerTest.class.getClassLoader(),
                new Class<?>[]{
                    Iterator.class
                }, new CustomInvocationHandler());

        try {
            it.remove();
            fail("should throw an unsupported operation exception");
        } catch (UnsupportedOperationException ex) {
            // ok
        }
    }

    @Test
    public void testCloneable() {
        assertSame(Proxy.getInvocationHandler(proxy.clone()), Proxy.getInvocationHandler(proxy));
    }

    private static interface C extends Cloneable {

        Object clone();

        void f();

    }

}
