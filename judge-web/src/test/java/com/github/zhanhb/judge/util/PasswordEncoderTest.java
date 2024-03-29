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

import com.github.zhanhb.judge.Application;
import lombok.extern.slf4j.Slf4j;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringApplicationConfiguration;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 *
 * @author zhanhb
 * @date Jun 4, 2015, 16:57:35
 */
@RunWith(SpringJUnit4ClassRunner.class)
@Slf4j
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class PasswordEncoderTest {

    private static final String ORIGN = "admin";
    private static final String SHA = "d033e22ae348aeb5660fc2140aec35850c4da997";
    private static final String BCRYPT = "$2a$10$TNkPaGUpWzppnfzArYQsVOKdqU1qTHlq3UGaCT3dFJ8fiIsaXwj3i";

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Test of encode method, of class PasswordEncoderImpl.
     */
    @Test
    public void testEncodePassword() {
        log.info("encodePassword");
        CharSequence rawPassword = ORIGN;
        String encodedPassword = SHA;
        assertTrue(passwordEncoder.matches(rawPassword, encodedPassword));
    }

    @Test
    public void testRawPassword() {
        log.info("encodePassword");

        String password = SHA;
        assertFalse(passwordEncoder.matches(password, password));
        password = password.substring(0, 20);
        assertTrue(passwordEncoder.matches(password, password));
    }

    @Test
    public void testBCrypt() {
        CharSequence rawPassword = ORIGN;
        String encodedPassword = BCRYPT;
        assertTrue(passwordEncoder.matches(rawPassword, encodedPassword));
        assertEquals(encodedPassword, BCrypt.hashpw(rawPassword.toString(), encodedPassword));
    }
}
