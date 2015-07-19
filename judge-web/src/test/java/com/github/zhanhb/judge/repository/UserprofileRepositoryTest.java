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
package com.github.zhanhb.judge.repository;

import com.github.zhanhb.judge.Application;
import com.github.zhanhb.judge.domain.Userprofile;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Date;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 *
 * @author zhanhb
 */
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class UserprofileRepositoryTest {

    private static final String handle = "testaccount1";
    private static final String password = "testpassword1";
    private static final String email = "testemail1@example.com";

    @Resource
    private UserprofileRepository userprofileRepository;
    @Resource
    private PasswordEncoder passwordEncoder;
    private long id;

    @Before
    public void setUp() {
        id = userprofileRepository.findByHandleIgnoreCase(handle).orElseGet(
                () -> userprofileRepository.save(
                        Userprofile.builder().handle(handle)
                        .password(passwordEncoder.encode(password))
                        .nickname("nick")
                        .email(email)
                        .school("school")
                        .major("major")
                        .build()
                )
        ).getId();
        log.info("id = " + id);
    }

    /**
     * Test of findByEmail method, of class UserprofileRepository.
     */
    @Test
    public void testFindByEmail() {
        log.info("findByEmail");
        Userprofile userprofile = userprofileRepository.findByEmail(email).get();
        assertTrue(passwordEncoder.matches(password, userprofile.getPassword()));
    }

    @Test
    public void testFindByHandle() {
        log.info("findByHandleIgnoreCase");
        Userprofile userprofile = userprofileRepository.findByHandleIgnoreCase(handle).get();
        assertTrue(passwordEncoder.matches(password, userprofile.getPassword()));
    }

    @Test
    public void testFindAll() {
        assertNotEquals(0, userprofileRepository.findAll().spliterator().estimateSize());
    }

    @Test
    public void testFindById() {
        assertTrue(userprofileRepository.findById(id).isPresent());
    }

    @Test
    public void testLastModified() {
        long now = System.currentTimeMillis() - 1_000; // substract one second to avoid the error
        Userprofile userprofile = userprofileRepository.findByHandleIgnoreCase(handle).get();
        userprofile.setPassword(passwordEncoder.encode(password));
        long modifiedTime = Date.from(userprofileRepository.save(userprofile).getLastUpdateDate().atZone(ZoneId.systemDefault()).toInstant()).getTime();
        assertTrue(Instant.ofEpochMilli(now) + " <= " + Instant.ofEpochMilli(modifiedTime), now <= modifiedTime);
        assertTrue(Instant.ofEpochMilli(modifiedTime) + " <= " + Instant.ofEpochMilli(now + 30_000), modifiedTime <= now + 30_000);
    }
}
