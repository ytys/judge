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

import java.util.function.Function;
import java.util.regex.Pattern;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author zhanhb
 */
public class MatcherWrapperTest {

    private void testReplaceAll(String pattern, String input, Function<MatcherWrapper, String> replaceFunction, String expectedResult) {
        String result = new MatcherWrapper(Pattern.compile(pattern), input).replaceAll(replaceFunction);
        assertEquals(expectedResult, result);
    }

    private void testReplaceFirst(String pattern, String input, Function<MatcherWrapper, String> replaceFunction, String expectedResult) {
        String result = new MatcherWrapper(Pattern.compile(pattern), input).replaceFirst(replaceFunction);
        assertEquals(expectedResult, result);
    }

    /**
     * Test of replaceAll method, of class MatcherWrapper.
     */
    @Test
    public void testReplaceAll_Function() {
        testReplaceAll("((?:he))", "(hezzhe)", matcher -> matcher.group(1) + "$1zhb", "(he$1zhbzzhe$1zhb)");
    }

    /**
     * Test of replaceFirst method, of class MatcherWrapper.
     */
    @Test
    public void testReplaceFirst_Function() {
        testReplaceFirst("((?:he))", "(hezzhe)", matcher -> matcher.group(1) + "$1zhb", "(he$1zhbzzhe)");
    }

    @Test
    public void testReplaceAllOriginal() {
        testReplaceAll("a+", "zzzz", matcher -> matcher.group(1), "zzzz");
    }

    @Test
    public void testReplaceFirstOriginal() {
        testReplaceFirst("a+", "zzzz", matcher -> matcher.group(1), "zzzz");
    }

    @Test(expected = IllegalStateException.class)
    public void testLocked() {
        testReplaceAll("a++", "aaaaa", matcher -> matcher.find() + "", "zzzz");
    }

}
