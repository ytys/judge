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
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.codec.Hex;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 *
 * @author zhanhb
 */
@RequiredArgsConstructor
public enum MessageDigestPasswordEncoder implements PasswordEncoder {

    MD5("MD5", 32),
    SHA1("SHA1", 40),
    SHA256("SHA-256", 64),
    SHA512("SHA-512", 128);

    private final String algorithm;
    private final int passwordLength;

    @Override
    public String encode(CharSequence password) {
        MessageDigest digest = getDigest(algorithm);
        byte[] bytes = password == null ? digest.digest() : digest.digest(password.toString()
                .getBytes(StandardCharsets.UTF_8));
        return new String(Hex.encode(bytes));
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return encodedPassword.length() == passwordLength
                && encode(rawPassword).equalsIgnoreCase(encodedPassword);
    }

    private MessageDigest getDigest(String algorithm) {
        try {
            return MessageDigest.getInstance(algorithm);
        } catch (NoSuchAlgorithmException ex) {
            throw new IllegalArgumentException("no such algorithm '" + algorithm + "'", ex);
        }
    }

}
