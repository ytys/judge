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

import com.github.zhanhb.judge.model.Contest;
import com.github.zhanhb.judge.model.enums.ContestType;
import com.github.zhanhb.judge.repository.ContestRepository;
import com.github.zhanhb.judge.testenv.AbstractMockMvcTests;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/**
 *
 * @author zhanhb
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
public class ContestControllerTest extends AbstractMockMvcTests {

    private static final String contestName = "testcontest1";

    @Autowired
    private ContestRepository repository;
    @Before
    public void setUp() {
        repository.findByNameIgnoreCase(contestName).orElseGet(
                () -> repository.save(Contest
                        .builder()
                        .type(ContestType.OJ)
                        .title("title")
                        .name(contestName)
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
        System.out.println("listAsJson");
        mockMvc.perform(get("/contest").accept(APPLICATION_JSON))
                .andExpect(content().contentType(APPLICATION_JSON));
    }

    /**
     * Test of viewAsJson method, of class ContestController.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testViewAsJson() throws Exception {
        mockMvc.perform(get("/contest/{name}", contestName).accept(APPLICATION_JSON))
                .andExpect(content().contentType(APPLICATION_JSON));
    }

    @Test
    public void testView404() throws Exception {
        webAppContextSetup(wac).build()
                .perform(get("/contest/{name}", contestName + "1"))
                .andExpect(status().isNotFound());
    }

}
