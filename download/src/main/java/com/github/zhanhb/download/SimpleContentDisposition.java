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

import java.io.CharArrayWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;
import java.util.BitSet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SimpleContentDisposition implements ContentDisposition {

    @Override
    public void setContentDisposition(HttpServletRequest request, HttpServletResponse response,
            String filename) {
        if (filename == null) {
            response.setHeader("Content-Disposition", "attachment");
            return;
        }
        if (filename.matches("^[\040-~\011\012\015&&[^%]]+$")) {
            response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
        } else {
            String encoded;
            try {
                encoded = Encoder.encode(filename, "utf-8", false);
            } catch (UnsupportedEncodingException ex) {
                throw new Error(ex);
            }
            response.setHeader("Content-Disposition", "attachment; filename=\"" + encoded + "\";  filename*=utf-8''" + encoded);
        }
    }

    private static class Encoder {

        private static final BitSet dontNeedEncoding;
        static final int caseDiff = ('a' - 'A');
        private static final char[] hexChars = "0123456789ABCDEF".toCharArray();

        static {

            /* The list of characters that are not encoded has been
             * determined as follows:
             *
             * RFC 2396 states:
             * -----
             * Data characters that are allowed in a URI but do not have a
             * reserved purpose are called unreserved.  These include upper
             * and lower case letters, decimal digits, and a limited set of
             * punctuation marks and symbols.
             *
             * unreserved  = alphanum | mark
             *
             * mark        = "-" | "_" | "." | "!" | "~" | "*" | "'" | "(" | ")"
             *
             * Unreserved characters can be escaped without changing the
             * semantics of the URI, but this should not be done unless the
             * URI is being used in a context that does not allow the
             * unescaped character to appear.
             * -----
             *
             * It appears that both Netscape and Internet Explorer escape
             * all special characters from this list with the exception
             * of "-", "_", ".", "*". While it is not clear why they are
             * escaping the other characters, perhaps it is safest to
             * assume that there might be contexts in which the others
             * are unsafe if not escaped. Therefore, we will use the same
             * list. It is also noteworthy that this is consistent with
             * O'Reilly's "HTML: The Definitive Guide" (page 164).
             *
             * As a last note, Intenet Explorer does not encode the "@"
             * character which is clearly not unreserved according to the
             * RFC. We are being consistent with the RFC in this matter,
             * as is Netscape.
             *
             */
            dontNeedEncoding = new BitSet(127);
            int i;
            for (i = 'a'; i <= 'z'; i++) {
                dontNeedEncoding.set(i);
            }
            for (i = 'A'; i <= 'Z'; i++) {
                dontNeedEncoding.set(i);
            }
            for (i = '0'; i <= '9'; i++) {
                dontNeedEncoding.set(i);
            }
            dontNeedEncoding.set(' '); /* encoding a space to a + is done
             * in the encode() method */

            dontNeedEncoding.set('-');
            dontNeedEncoding.set('_');
            dontNeedEncoding.set('.');
            dontNeedEncoding.set('*');
        }

        @SuppressWarnings("NestedAssignment")
        public static String encode(String s, String enc, boolean convertWhiteSpaceToPlus)
                throws UnsupportedEncodingException {

            boolean needToChange = false;
            StringBuilder out = new StringBuilder(s.length());
            CharArrayWriter charArrayWriter = new CharArrayWriter();

            if (enc == null) {
                throw new NullPointerException("charsetName");
            }

            Charset charset;
            try {
                charset = Charset.forName(enc);
            } catch (IllegalCharsetNameException e) {
                throw new UnsupportedEncodingException(enc);
            } catch (UnsupportedCharsetException e) {
                throw new UnsupportedEncodingException(enc);
            }

            for (int i = 0; i < s.length();) {
                int c = s.charAt(i);
                if (dontNeedEncoding.get(c)) {
                    if (convertWhiteSpaceToPlus && c == ' ') {
                        c = '+';
                        needToChange = true;
                    }
                    out.append((char) c);
                    i++;
                } else {
                    // convert to external encoding before hex conversion
                    do {
                        charArrayWriter.write(c);
                        i++;
                    } while (i < s.length() && !dontNeedEncoding.get((c = s.charAt(i))));

                    charArrayWriter.flush();
                    String str = new String(charArrayWriter.toCharArray());
                    byte[] ba = str.getBytes(charset.name());
                    for (int j = 0, len = ba.length; j < len; j++) {
                        out.append('%').append(hexChars[ba[j] >> 4 & 15]).append(hexChars[ba[j] & 15]);
                    }
                    charArrayWriter.reset();
                    needToChange = true;
                }
            }
            return (needToChange ? out.toString() : s);
        }

        private Encoder() {
        }
    }

}
