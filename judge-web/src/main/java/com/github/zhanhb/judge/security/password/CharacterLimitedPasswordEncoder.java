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
package com.github.zhanhb.judge.security.password;

import org.springframework.security.crypto.password.PasswordEncoder;

/**
 *
 * @author zhanhb
 */
public class CharacterLimitedPasswordEncoder extends PasswordEncoderWrapper {

    private final char[] limitChars;

    public CharacterLimitedPasswordEncoder(PasswordEncoder parent, String limitString) {
        super(parent);
        if (limitString.length() == 0) {
            throw new IllegalArgumentException("limitString");
        }
        this.limitChars = limitString.toCharArray();
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return super.matches(limit(rawPassword), encodedPassword);
    }

    @Override
    public String encode(CharSequence rawPassword) {
        return super.encode(limit(rawPassword));
    }

    private CharSequence limit(CharSequence encodedPassword) {
        String string = encodedPassword.toString();
        for (char ch : limitChars) {
            string = string.replace(ch, ' ');
        }
        return string;
    }

}
