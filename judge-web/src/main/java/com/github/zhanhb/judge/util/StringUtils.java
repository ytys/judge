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
public class StringUtils {

    public static String trimToNull(String str) {
        String trim = trim(str);
        if (trim == null || trim.length() == 0) {
            return null;
        }
        return trim;
    }

    public static String trimToEmpty(String str) {
        String trim = trim(str);
        return trim == null ? "" : trim;
    }

    public static String trim(String str) {
        if (str != null) {
            int len = str.length();
            int st = 0;

            while ((st < len) && Character.isWhitespace(str.charAt(st))) {
                st++;
            }
            while ((st < len) && Character.isWhitespace(str.charAt(len - 1))) {
                len--;
            }
            return (st > 0 || len < str.length()) ? str.substring(st, len) : str;
        }
        return str;
    }

    public static String slice(String string, int beginIndex) throws NullPointerException {
        return string.substring(fixIndex(string, beginIndex));
    }

    public static String slice(String string, int beginIndex, int endIndex) throws NullPointerException {
        int fixedBeginIndex = fixIndex(string, beginIndex);
        int fixedEndIndex = fixIndex(string, endIndex);
        if (fixedBeginIndex >= fixedEndIndex) {
            return "";
        }
        return string.substring(fixedBeginIndex, fixedEndIndex);
    }

    private static int fixIndex(String str, int index) {
        int len = str.length();
        return (index >= 0)
                ? Math.min(len, index)
                : Math.max(0, len + index);
    }

}
