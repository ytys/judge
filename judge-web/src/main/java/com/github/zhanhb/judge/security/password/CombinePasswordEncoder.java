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

import java.util.Objects;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 *
 * @author zhanhb
 */
public class CombinePasswordEncoder extends PasswordEncoderWrapper {

    private final PasswordEncoder[] encoders;

    public CombinePasswordEncoder(PasswordEncoder... passwordEncoders) {
        super(passwordEncoders[0]);
        // null check
        PasswordEncoder[] clone = passwordEncoders.clone();
        for (PasswordEncoder encoder : clone) {
            Objects.requireNonNull(encoder);
        }
        encoders = clone;
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        for (PasswordEncoder encoder : encoders) {
            Class<?> cl = encoder.getClass();
            if (cl == BCryptPasswordEncoder.class && !encodedPassword.startsWith("$2")) { // fast failed
                continue;
            }
            if (cl == MessageDigestPasswordEncoder.class) { // fast failed
                MessageDigestPasswordEncoder mdpe = (MessageDigestPasswordEncoder) encoder;
                int digestLength = mdpe.getPasswordLength();
                if (digestLength > 0 && digestLength != encodedPassword.length()) {
                    continue;
                }
                // fail through
            }
            if (encoder.matches(rawPassword, encodedPassword)) {
                return true;
            }
        }
        return false;
    }
}
