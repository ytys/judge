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
import com.github.zhanhb.judge.domain.Contest;
import com.github.zhanhb.judge.domain.ContestType;
import com.github.zhanhb.judge.repository.ContestRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.AssumptionViolatedException;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringApplicationConfiguration;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;
import org.springframework.util.ClassUtils;
import org.springframework.web.context.WebApplicationContext;

/**
 *
 * @author zhanhb
 */
@RunWith(SpringJUnit4ClassRunner.class)
@Slf4j
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class ContestControllerTest {

    private static final String CONTEST_NAME = "testcontest1";

    @BeforeClass
    public static void setUpClass() {
        if (!ClassUtils.isPresent("org.springframework.data.rest.webmvc.config.RepositoryRestMvcConfiguration", Thread.currentThread().getContextClassLoader())) {
            throw new AssumptionViolatedException("rest not on the classpath, skip the test");
        }
    }

    @Autowired
    private WebApplicationContext wac;
    @Autowired
    private ContestRepository contests;
    private MockMvc mockMvc;

    @Before
    public void setUp() {
        mockMvc = webAppContextSetup(wac).alwaysExpect(status().isOk()).build();
        contests.findByNameIgnoreCase(CONTEST_NAME).orElseGet(
                () -> contests.save(Contest
                        .builder()
                        .type(ContestType.OJ)
                        .title("title")
                        .name(CONTEST_NAME)
                        .build()
                )
        );
    }

    /**
     * Test of listAsJson method, of class ContestController.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testListAsJson() throws Exception {
        log.info("listAsJson");
        mockMvc.perform(get("/rest/contests").accept(APPLICATION_JSON))
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON));
    }

    /**
     * Test of viewAsJson method, of class ContestController.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testViewAsJson() throws Exception {
        mockMvc.perform(get("/rest/contests/search/findByNameIgnoreCase?name={name}", CONTEST_NAME).accept(APPLICATION_JSON))
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON));
    }

    @Test
    public void testView404() throws Exception {
        webAppContextSetup(wac).build()
                .perform(get("/contests/search/findByNameIgnoreCase?name={name}", CONTEST_NAME + "1"))
                .andExpect(status().isNotFound());
    }

}
