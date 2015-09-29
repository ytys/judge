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
package com.github.zhanhb.judge.web;

import com.github.zhanhb.judge.Application;
import com.github.zhanhb.judge.domain.Userprofile;
import com.github.zhanhb.judge.repository.UserprofileRepository;
import com.github.zhanhb.judge.repository.UserprofileRoleRepository;
import lombok.extern.slf4j.Slf4j;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;
import org.springframework.web.context.WebApplicationContext;

/**
 *
 * @author zhanhb
 */
@RunWith(SpringJUnit4ClassRunner.class)
@Slf4j
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class RegisterControllerTest {

    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;
    @Autowired
    private UserprofileRepository users;
    @Autowired
    private UserprofileRoleRepository urr;

    @Before
    public void setup() throws Exception {
        mockMvc = webAppContextSetup(wac).build();
    }

    /**
     * Test of registerView method, of class RegisterController.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testRegisterView() throws Exception {
        log.info("registerView");
        mockMvc.perform(get("/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"));
    }

    /**
     * Test of register method, of class RegisterController.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testRegister() throws Exception {
        log.info("register");
        String email = "example@test.com";
        assertThat(users.findByEmail(email).isPresent(), is(false));
        mockMvc.perform(post("/register")
                .param("handle", "random_handle111")
                .param("password", "pass123456")
                .param("rptPassword", "pass123456")
                .param("email", email))
                .andDo(print())
                .andExpect(status().isFound());

        Userprofile userprofile = users.findByEmail(email).get();
        urr.delete(urr.findAllByUserprofile(userprofile));
        users.delete(userprofile);
    }

}
