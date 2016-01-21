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

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 *
 * @author zhanhb
 */
public enum MessageDigestPasswordEncoder implements PasswordEncoder {

    MD5("MD5"),
    SHA1("SHA1"),
    SHA256("SHA-256");

    private final char[] bytes = {
        '0', '1', '2', '3', '4', '5', '6', '7',
        '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'
    };

    private final String algorithm;
    private final int passwordLength;

    MessageDigestPasswordEncoder(String algorithm) {
        MessageDigest digest = getDigest(algorithm);
        this.algorithm = digest.getAlgorithm();
        passwordLength = digest.getDigestLength() << 1; // hex bytes
    }

    public int getPasswordLength() {
        return passwordLength;
    }

    @Override
    public String encode(CharSequence password) {
        return hexBytes(getDigest(algorithm)
                .digest(password.toString()
                        .getBytes(StandardCharsets.UTF_8)));
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return encode(rawPassword).equalsIgnoreCase(encodedPassword);
    }

    private String hexBytes(byte[] digest) {
        int len = digest.length;
        char[] str = new char[len << 1];
        for (int i = 0; i < len; i++) {
            str[i << 1] = bytes[digest[i] >> 4 & 15];
            str[i << 1 | 1] = bytes[digest[i] & 15];
        }
        return new String(str);
    }

    private MessageDigest getDigest(String algorithm) {
        try {
            return MessageDigest.getInstance(algorithm);
        } catch (NoSuchAlgorithmException ex) {
            throw new IllegalArgumentException("no such algorithm '" + algorithm + "'", ex);
        }
    }

}
