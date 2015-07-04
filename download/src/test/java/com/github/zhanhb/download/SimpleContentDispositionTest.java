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
public class SimpleContentDispositionTest {

    public static boolean isSurrogate(int ch) {
        return (ch >= 55296) && (ch < 57344);
    }

    /**
     *
     * @param s
     * @param enc
     * @return
     * @throws java.io.UnsupportedEncodingException
     * @see SimpleContentDisposition.Encoder#encode(String, String, boolean)
     */
    public String encode(String s, String enc) throws UnsupportedEncodingException {
        return SimpleContentDisposition.Encoder.encode(s, enc);
    }

    /**
     * Test of setContentDisposition method, of class SimpleContentDisposition.
     *
     * @throws java.lang.Exception
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testEncoder() throws Exception {
        Charset UTF_8 = Charset.forName("UTF-8");

        int len = 128;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; ++i) {
            sb.append((char) i);
        }
        Random random = new Random();
        for (int i = 0; i < 10000; ++i) {
            int ch = random.nextInt(10 * 65536 - 128) + 128;
            if (isSurrogate(ch)) {
                continue;
            }
            sb.append(Character.toChars(ch));
        }

        String s = sb.toString();

        String enc = UTF_8.name();
        String encoded = encode(s, enc);

        for (char ch : encoded.toCharArray()) {
            assertTrue(32 < ch && ch < 127);
        }

        String decode = URLDecoder.decode(encoded, enc);
        assertEquals(s, decode);

        char[] arr = encoded.replaceAll("%[a-z0-9]{2}", "").toCharArray();
        BitSet test = new BitSet(128);
        for (char b : arr) {
            test.set(b);
        }

        BitSet sub = test.get('a', 'z' + 1);
        sub.or(test.get('A', 'Z' + 1));
        sub.or(test.get('0', '9' + 1));
        assertEquals(64, sub.size());
    }

}
