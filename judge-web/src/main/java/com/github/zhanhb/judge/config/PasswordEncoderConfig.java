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
package com.github.zhanhb.judge.config;

import com.github.zhanhb.judge.security.password.CombinePasswordEncoder;
import com.github.zhanhb.judge.security.password.LengthLimitedPasswordEncoder;
import com.github.zhanhb.judge.security.password.MessageDigestPasswordEncoder;
import com.github.zhanhb.judge.security.password.MutiPasswordSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 *
 * @author zhanhb
 */
@Configuration
public class PasswordEncoderConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new LengthLimitedPasswordEncoder(
                new CombinePasswordEncoder(1,
                        NoOpPasswordEncoder.getInstance(),
                        new MutiPasswordSupport(
                                new CombinePasswordEncoder(
                                        new BCryptPasswordEncoder(6),
                                        MessageDigestPasswordEncoder.SHA1,
                                        MessageDigestPasswordEncoder.MD5
                                )
                        )
                ), 30);
    }

}
