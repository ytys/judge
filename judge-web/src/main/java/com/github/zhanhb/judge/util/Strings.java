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

/**
 *
 * @author zhanhb
 */
@SuppressWarnings("ClassWithoutLogger")
public class Strings {

    public static String trimLeft(String str) {
        if (str != null) {
            int len = str.length();
            int st = 0;

            while ((st < len) && (str.charAt(st) <= ' ')) {
                st++;
            }
            if (st > 0) {
                return str.substring(st, len);
            }
        }
        return str;
    }

    public static String trimRight(String str) {
        if (str != null) {
            int len = str.length();

            while (0 < len && str.charAt(len - 1) <= ' ') {
                len--;
            }
            if (len < str.length()) {
                return str.substring(0, len);
            }
        }
        return str;
    }

    public static String trimToNull(String str) {
        final String ts = trim(str);
        return isEmpty(ts) ? null : ts;
    }

    public static boolean isEmpty(final CharSequence cs) {
        return cs == null || cs.length() == 0;
    }

    public static String trimToEmpty(String str) {
        return str == null ? "" : str.trim();
    }

    public static String trim(String str) {
        return str != null ? str.trim() : null;
    }

    public static String slice(String string, int beginIndex) {
        return string == null ? null : string.substring(fixIndex(string, beginIndex));
    }

    public static String slice(String string, int beginIndex, int endIndex) {
        if (string == null) {
            return null;
        }
        int fixedBeginIndex = fixIndex(string, beginIndex);
        int fixedEndIndex = fixIndex(string, endIndex);
        if (fixedBeginIndex >= fixedEndIndex) {
            return "";
        }
        return string.substring(fixedBeginIndex, fixedEndIndex);
    }

    private static int fixIndex(String str, int index) {
        return (index >= 0)
                ? Math.min(str.length(), index)
                : Math.max(0, str.length() + index);
    }

    private Strings() {
        throw new AssertionError();
    }

}
