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

import static com.github.zhanhb.judge.util.Numbers.addIgnoreOverFlow;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import lombok.extern.slf4j.Slf4j;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import org.junit.Test;

/**
 *
 * @author zhanhb
 */
@Slf4j
public class NumbersTest {

    @Test
    public void testConstructor() throws Throwable {
        Constructor<Numbers> c = Numbers.class.getDeclaredConstructor();
        c.setAccessible(true);
        try {
            c.newInstance();
            fail("should throw an InvocationTargetException");
        } catch (InvocationTargetException ex) {
            assertThat(ex.getTargetException(), instanceOf(AssertionError.class));
        }
    }

    /**
     * Test of addIgnoreOverFlow method, of class Numbers.
     */
    @Test
    public void testAddIgnoreOverFlow_int_int() {
        log.info("addIgnoreOverFlow");
        assertEquals(0, addIgnoreOverFlow(0, 0));
        assertEquals(10, addIgnoreOverFlow(5, 5));

        assertEquals(Integer.MAX_VALUE, addIgnoreOverFlow(Integer.MAX_VALUE, 5));
        assertEquals(Integer.MIN_VALUE, addIgnoreOverFlow(Integer.MIN_VALUE, Integer.MIN_VALUE));
        assertEquals(Integer.MIN_VALUE, addIgnoreOverFlow(Integer.MIN_VALUE + 1, Integer.MIN_VALUE + 1));
        assertEquals(-1, addIgnoreOverFlow(Integer.MAX_VALUE, Integer.MIN_VALUE));
    }

    /**
     * Test of addIgnoreOverFlow method, of class Numbers.
     */
    @Test
    public void testAddIgnoreOverFlow_long_long() {
        log.info("addIgnoreOverFlow");
        assertEquals(0, addIgnoreOverFlow(0L, 0L));
        assertEquals(10L, addIgnoreOverFlow(5L, 5L));

        assertEquals(Long.MAX_VALUE, addIgnoreOverFlow(Long.MAX_VALUE, 5));
        assertEquals(Long.MIN_VALUE, addIgnoreOverFlow(Long.MIN_VALUE, Long.MIN_VALUE));
        assertEquals(Long.MIN_VALUE, addIgnoreOverFlow(Long.MIN_VALUE + 1, Long.MIN_VALUE + 1));
        assertEquals(-1, addIgnoreOverFlow(Long.MAX_VALUE, Long.MIN_VALUE));
    }

}
