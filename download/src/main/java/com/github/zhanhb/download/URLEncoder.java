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

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.BitSet;

/**
 *
 * @author zhanhb
 */
public class URLEncoder {

    // https://tools.ietf.org/html/rfc5987#section-3.2
    // we will encoding + for some browser will decode + to a space
    public static final URLEncoder CONTENT_DISPOSITION = new URLEncoder("!#$&-.^_`|~");
    private static final char[] hexChars = "0123456789ABCDEF".toCharArray();

    private final BitSet dontNeedEncoding;

    public URLEncoder() {
        BitSet bs = new BitSet(128);
        bs.set('a', 'z' + 1);
        bs.set('A', 'Z' + 1);
        bs.set('0', '9' + 1);
        dontNeedEncoding = bs;
    }

    public URLEncoder(String dontNeedEncoding) {
        this();
        for (int i = 0, len = dontNeedEncoding.length(); i < len; ++i) {
            this.dontNeedEncoding.set(dontNeedEncoding.charAt(i));
        }
    }

    /**
     *
     * @param s
     * @param charset
     * @return
     * @throws IllegalArgumentException if the string s has surrogate char but
     * not a valid surrogate pair
     * @see Character#isHighSurrogate(char)
     * @see Character#isLowSurrogate(char)
     */
    public String encode(String s, Charset charset) {
        boolean needToChange = false;
        final int length = s.length();
        StringBuilder out = new StringBuilder(length);
        CharsetEncoder encoder = charset.newEncoder();

        for (int cur = 0; cur < length;) {
            int start = cur;
            while (cur < length && dontNeedEncoding.get(s.charAt(cur))) {
                ++cur;
            }
            if (start != cur) {
                out.append(s, start, cur);
                start = cur;
            }
            while (cur < length && !dontNeedEncoding.get(s.charAt(cur))) {
                ++cur;
            }
            if (start != cur) {
                // convert to external encoding before hex conversion
                ByteBuffer buffer;
                try {
                    buffer = encoder.encode(CharBuffer.wrap(s, start, cur));
                } catch (CharacterCodingException ex) {
                    throw new IllegalArgumentException(s, ex);
                }
                int j = buffer.position();
                int limit = buffer.limit();
                for (; j < limit; ++j) {
                    byte b = buffer.get(j);
                    out.append('%').append(hexChars[b >> 4 & 0xF]).append(hexChars[b & 0xF]);
                }
                needToChange = true;
            }
        }
        return needToChange ? out.toString() : s;
    }

    public String encode(String s) {
        return encode(s, Charset.forName("utf-8"));
    }

}
