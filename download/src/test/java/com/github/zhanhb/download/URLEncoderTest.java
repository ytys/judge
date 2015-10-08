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
package com.github.zhanhb.download;

import java.io.UnsupportedEncodingException;
import static java.lang.Character.MAX_SURROGATE;
import static java.lang.Character.MIN_SURROGATE;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.BitSet;
import java.util.Random;
import java.util.function.IntPredicate;
import java.util.function.IntUnaryOperator;
import java.util.stream.IntStream;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 *
 * @author zhanhb
 */
public class URLEncoderTest {

    private static final int SURROGATE_DIFF = MAX_SURROGATE + 1 - MIN_SURROGATE;

    /**
     * Test of encode method, of class URLEncoder.
     *
     * @throws java.io.UnsupportedEncodingException
     */
    @Test
    public void testEncoder() throws UnsupportedEncodingException {
        IntPredicate isSurrogate = (int ch) -> {
            return ch >= MIN_SURROGATE && ch < (MAX_SURROGATE + 1);
        };
        IntUnaryOperator addSurrogate = x
                -> x >= MIN_SURROGATE ? x + SURROGATE_DIFF : x;

        Charset charset = StandardCharsets.UTF_8;

        int len = 128;
        int num2 = 1000000;
        StringBuilder sb = new StringBuilder(len + num2 * 2);
        IntStream.range(0, len).forEach(i -> sb.append((char) i));

        int cpStart = 128;
        int maxcodepoint = 10 * 65536;
        new Random().ints(cpStart, maxcodepoint - SURROGATE_DIFF)
                .map(addSurrogate)
                .map(x -> {
                    assertTrue(x + " >= " + (int) MAX_SURROGATE
                            + " || " + x + "<=" + (int) MIN_SURROGATE + " failed", !isSurrogate.test(x));
                    assertTrue(x >= cpStart);
                    assertTrue(x < maxcodepoint);
                    return x;
                })
                .mapToObj(Character::toChars)
                .limit(num2)
                .forEach(sb::append);

        String s = sb.toString();

        String encoded = SimpleContentDisposition.CONTENT_DISPOSITION.encode(s, charset);

        encoded.chars().forEach(ch -> assertTrue(32 < ch && ch < 127));

        String decoded = URLDecoder.decode(encoded, charset.name());
        assertEquals(s, decoded);

        BitSet test = new BitSet(128);
        encoded.replaceAll("%[a-zA-Z0-9]{2}", "").chars().forEach(test::set);

        BitSet tmp = new BitSet(128);
        tmp.set('a', 'z' + 1); // 26
        tmp.set('A', 'Z' + 1); // 26
        tmp.set('0', '9' + 1); // 10
        tmp.and(test);
        assertEquals(26 * 2 + 10, tmp.cardinality());
    }

}
