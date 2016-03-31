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
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;
import org.springframework.web.context.WebApplicationContext;

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

    @Autowired
    private UserprofileRepository userprofiles;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private WebApplicationContext context;
    private long id;

    @Before
    public void setUp() {
        id = userprofiles.findByHandleIgnoreCase(handle).orElseGet(
                () -> userprofiles.save(
                        Userprofile.builder().handle(handle)
                        .password(passwordEncoder.encode(password))
                        .nickname("nick")
                        .email(email)
                        .school("school")
                        .major("major")
                        .build()
                )
        ).getId();
        log.info("id = {}", id);
    }

    /**
     * Test of findByEmail method, of class UserprofileRepository.
     */
    @Test
    public void testFindByEmail() {
        log.info("findByEmail");
        Userprofile userprofile = userprofiles.findByEmail(email).get();
        assertTrue(passwordEncoder.matches(password, userprofile.getPassword()));
    }

    @Test
    public void testFindByHandle() {
        log.info("findByHandleIgnoreCase");
        Userprofile userprofile = userprofiles.findByHandleIgnoreCase(handle).get();
        assertTrue(passwordEncoder.matches(password, userprofile.getPassword()));
    }

    @Test
    public void testFindAll() {
        assertNotEquals(0, userprofiles.findAll().spliterator().estimateSize());
    }

    @Test
    public void testFindById() {
        assertTrue(userprofiles.findOne(id).isPresent());
    }

    @Test
    public void testLastModified() {
        long now = System.currentTimeMillis() - 1_000; // substract one second to avoid the error
        Userprofile userprofile = userprofiles.findByHandleIgnoreCase(handle).get();
        userprofile.setPassword(passwordEncoder.encode(password));
        LocalDateTime lastUpdateDate = userprofiles.save(userprofile).getLastUpdateDate();
        long modifiedTime = Date.from(lastUpdateDate.atZone(ZoneId.systemDefault()).toInstant()).getTime();
        assertTrue(Instant.ofEpochMilli(now) + " <= " + Instant.ofEpochMilli(modifiedTime), now <= modifiedTime);
        assertTrue(Instant.ofEpochMilli(modifiedTime) + " <= " + Instant.ofEpochMilli(now + 30_000), modifiedTime <= now + 30_000);
    }

    @Test
    public void testQueryDsl() throws Exception {
        MockMvc mvc = webAppContextSetup(context).build();
        mvc.perform(get("/rest/userprofiles").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

}
