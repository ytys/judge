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

import com.github.zhanhb.judge.model.Problem;
import com.github.zhanhb.judge.repository.ProblemRepository;
import com.github.zhanhb.judge.testenv.AbstractMockMvcTests;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_XML;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/**
 *
 * @author zhanhb
 * @date Jun 5, 2015, 2:20:42
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
public class ProblemControllerTest extends AbstractMockMvcTests {

    private static final String PROBLEM_LIST = "/problem";
    private static final String PROBLEM_VIEW = "/problem/{problemId}";

    private String encoding = "ISO-8859-1";

    @Autowired
    private ProblemRepository repository;
    private long id = -1;

    @Before
    public void setup() throws Exception {
        if (id == -1) {
            id = repository.save(
                    Problem.builder()
                    .title("title")
                    .description("description")
                    .input("input")
                    .output("output")
                    .sampleInput("sampleInput")
                    .sampleOutput("sampleOutput")
                    .hint("hint")
                    .source("source")
                    .build()).getId();
        }

    }

    @Test
    public void testListAsJson() throws Exception {
        mockMvc.perform(get(PROBLEM_LIST).accept(APPLICATION_JSON))
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(content().encoding(encoding));
    }

    @Test
    public void testList() throws Exception {
        mockMvc.perform(get(PROBLEM_LIST))
                .andExpect(view().name("problem/list"));
    }

    @Test
    public void testView() throws Exception {
        mockMvc.perform(get(PROBLEM_VIEW, id))
                .andExpect(view().name("problem/view"));
    }

    @Test
    public void testView404() throws Exception {
        MockMvc mvc = webAppContextSetup(wac)
                .alwaysExpect(status().isNotFound()).build();

        mvc.perform(get(PROBLEM_VIEW, id + 1));

        mvc.perform(get(PROBLEM_VIEW, id + 1).accept(APPLICATION_XML));

        mvc.perform(get(PROBLEM_VIEW, id + 1).accept(APPLICATION_JSON));
    }

    @Test
    public void testViewAsXml() throws Exception {
        mockMvc.perform(get(PROBLEM_VIEW, id).accept(APPLICATION_XML))
                .andExpect(content().contentType(APPLICATION_XML));
    }
}
