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
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author zhanhb
 * @see https://tools.ietf.org/html/rfc6266#page-4
 */
public class SimpleContentDisposition implements ContentDisposition {

    @Override
    public void setContentDisposition(HttpServletResponse response, String filename) {
        if (filename == null || filename.length() == 0) {
            response.setHeader("Content-Disposition", "attachment");
        } else if (isToken(filename)) { // already a token
            response.setHeader("Content-Disposition", "attachment; filename=" + filename);
        } else {
            String encoded = Encoder.encode(filename, Charset.forName("utf-8"));
            response.setHeader("Content-Disposition", "attachment; filename=\"" + encoded + "\"; filename*=utf-8''" + encoded);
        }
    }

    private boolean isToken(String filename) {
        if (filename == null || filename.length() == 0) {
            return false;
        }
        for (int i = 0, len = filename.length(); i < len; ++i) {
            char ch = filename.charAt(i);
            if (ch >= 0x7F || ch < ' ') { // CHAR predicate
                return false;
            }
            switch (ch) {
                // token separators
                // @see https://tools.ietf.org/html/rfc2616#section-2.2
                case '(':
                case ')':
                case '<':
                case '>':
                case '@':
                case ',':
                case ';':
                case ':':
                case '\\':
                case '"':
                case '/':
                case '[':
                case ']':
                case '?':
                case '=':
                case '{':
                case '}':
                case ' ':
                case '\t':
                // should percent be a valid token???
                // here we are different from the rfc
                case '%':
                    return false;
            }
        }
        return true;
    }

    @SuppressWarnings("PackageVisibleInnerClass")
    static class Encoder {

        private static final BitSet dontNeedEncoding;
        private static final char[] hexChars = "0123456789ABCDEF".toCharArray();

        static {
            dontNeedEncoding = new BitSet(128);
            // https://tools.ietf.org/html/rfc5987#section-3.2
            dontNeedEncoding.set('a', 'z' + 1);
            dontNeedEncoding.set('A', 'Z' + 1);
            dontNeedEncoding.set('0', '9' + 1);

            dontNeedEncoding.set('!');
            dontNeedEncoding.set('#');
            dontNeedEncoding.set('$');
            dontNeedEncoding.set('&');
            // we will encoding + for some browser will decode + to a space
            // dontNeedEncoding.set('+');
            dontNeedEncoding.set('-');
            dontNeedEncoding.set('.');
            dontNeedEncoding.set('^');
            dontNeedEncoding.set('_');
            dontNeedEncoding.set('`');
            dontNeedEncoding.set('|');
            dontNeedEncoding.set('~');
        }

        /**
         *
         * @param s
         * @param charset
         * @return
         * @throws IllegalArgumentException if the string s has surrogate char but not a valid surrogate pair
         * @see Character#isHighSurrogate(char)
         * @see Character#isLowSurrogate(char)
         */
        public static String encode(String s, Charset charset) {
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
                while (cur < length && !dontNeedEncoding.get((s.charAt(cur)))) {
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
                    int j = buffer.position(), limit = buffer.limit();
                    for (; j < limit; ++j) {
                        byte b = buffer.get(j);
                        out.append('%').append(hexChars[b >> 4 & 15]).append(hexChars[b & 15]);
                    }
                    needToChange = true;
                }
            }
            return needToChange ? out.toString() : s;
        }

        private Encoder() {
        }
    }

}
