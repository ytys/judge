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
import java.nio.charset.CodingErrorAction;
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
            String encoded = Encoder.encode(filename, "utf-8");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + encoded + "\"; filename*=utf-8''" + encoded);
        }
    }

    private boolean isToken(String filename) {
        if (filename == null || filename.length() == 0) {
            return false;
        }
        for (int i = 0, len = filename.length(); i < len; ++i) {
            char ch = filename.charAt(i);
            if (ch >= 0x80 || ch <= ' ') {
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
                case '\u007F':
                    return false;
            }
        }
        return true;
    }

    @SuppressWarnings("PackageVisibleInnerClass")
    static class Encoder {

        private static final BitSet dontNeedEncoding;
        static final int caseDiff = ('a' - 'A');
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
            // dontNeedEncoding.set('+'); // we will encoding +
            dontNeedEncoding.set('-');
            dontNeedEncoding.set('.');
            dontNeedEncoding.set('^');
            dontNeedEncoding.set('_');
            dontNeedEncoding.set('`');
            dontNeedEncoding.set('|');
            dontNeedEncoding.set('~');
        }

        public static String encode(String s, String enc) {
            boolean needToChange = false;
            StringBuilder out = new StringBuilder(s.length());

            CharsetEncoder encoder = Charset.forName(enc).newEncoder()
                    .onMalformedInput(CodingErrorAction.REPLACE)
                    .onUnmappableCharacter(CodingErrorAction.REPLACE);

            for (int cur = 0, length = s.length(); cur < length;) {
                int start = cur;
                while (cur < length && dontNeedEncoding.get(s.charAt(cur))) {
                    ++cur;
                }
                if (start != cur) {
                    out.append(s, start, cur);
                    start = cur;
                }
                // convert to external encoding before hex conversion
                while (cur < length && !dontNeedEncoding.get((s.charAt(cur)))) {
                    ++cur;
                }
                if (start != cur) {
                    ByteBuffer buffer;
                    try {
                        buffer = encoder.encode(CharBuffer.wrap(s, start, cur));
                    } catch (CharacterCodingException ex) {
                        throw new Error(ex);
                    }
                    byte[] ba = buffer.array();
                    for (int bs = buffer.arrayOffset(), be = buffer.remaining() + buffer.arrayOffset(); bs < be; bs++) {
                        out.append('%').append(hexChars[ba[bs] >> 4 & 15]).append(hexChars[ba[bs] & 15]);
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
