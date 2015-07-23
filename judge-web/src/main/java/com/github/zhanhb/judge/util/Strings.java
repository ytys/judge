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

import lombok.experimental.UtilityClass;

/**
 *
 * @author zhanhb
 */
@UtilityClass
@SuppressWarnings("FinalClass")
public class Strings {

    public String trimLeft(String str) {
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

    public String trimRight(String str) {
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

    public String trimToNull(String str) {
        final String ts = trim(str);
        return isEmpty(ts) ? null : ts;
    }

    public boolean isEmpty(final CharSequence cs) {
        return cs == null || cs.length() == 0;
    }

    public String trimToEmpty(String str) {
        return str == null ? "" : str.trim();
    }

    public String trim(String str) {
        return str != null ? str.trim() : null;
    }

    public String slice(String string, int beginIndex) throws NullPointerException {
        return string.substring(fixIndex(string, beginIndex));
    }

    public String slice(String string, int beginIndex, int endIndex) throws NullPointerException {
        int fixedBeginIndex = fixIndex(string, beginIndex);
        int fixedEndIndex = fixIndex(string, endIndex);
        if (fixedBeginIndex >= fixedEndIndex) {
            return "";
        }
        return string.substring(fixedBeginIndex, fixedEndIndex);
    }

    private int fixIndex(String str, int index) {
        int len = str.length();
        return (index >= 0)
                ? Math.min(len, index)
                : Math.max(0, len + index);
    }

}
