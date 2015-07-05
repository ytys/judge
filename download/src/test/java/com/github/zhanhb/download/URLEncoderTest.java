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
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.BitSet;
import java.util.Random;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 *
 * @author zhanhb
 */
public class URLEncoderTest {

    public static boolean isSurrogate(int ch) {
        return (ch >= 55296) && (ch < 57344);
    }

    /**
     * Test of encode method, of class URLEncoder.
     *
     * @throws java.io.UnsupportedEncodingException
     */
    @Test
    public void testEncoder() throws UnsupportedEncodingException {
        Charset charset = Charset.forName("UTF-8");

        int len = 128;
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; ++i) {
            sb.append((char) i);
        }
        Random random = new Random();
        for (int i = 0; i < 10000000; ++i) {
            int ch = random.nextInt(10 * 65536 - 128) + 128;
            if (isSurrogate(ch)) {
                continue;
            }
            sb.append(Character.toChars(ch));
        }

        String s = sb.toString();

        String encoded = URLEncoder.CONTENT_DISPOSITION.encode(s, charset);

        for (char ch : encoded.toCharArray()) {
            assertTrue(32 < ch && ch < 127);
        }

        String decoded = URLDecoder.decode(encoded, charset.name());
        assertEquals(s, decoded);

        char[] arr = encoded.replaceAll("%[a-zA-Z0-9]{2}", "").toCharArray();
        BitSet test = new BitSet(128);
        for (char b : arr) {
            test.set(b);
        }

        BitSet tmp = new BitSet(128);
        tmp.set('a', 'z' + 1); // 26
        tmp.set('A', 'Z' + 1); // 26
        tmp.set('0', '9' + 1); // 10
        tmp.and(test);
        assertEquals(26 * 2 + 10, tmp.cardinality());
    }

}
