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
package com.github.zhanhb.judge.web.rest;

import ch.qos.logback.classic.Level;
import com.github.zhanhb.judge.Application;
import com.github.zhanhb.judge.repository.LoggerRepository;
import lombok.extern.slf4j.Slf4j;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.http.MediaType;
import static org.springframework.http.MediaType.APPLICATION_XML;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
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
public class LoggerControllerTest {

    private static final String logs = "/api/logs";

    @Autowired
    private WebApplicationContext context;
    @Autowired
    private RepositoryRestConfiguration restConfig;
    @Autowired
    private LoggerRepository loggers;
    private MockMvc mvc;

    @Before
    public void setUp() {
        this.mvc = webAppContextSetup(context).build();
    }

    /**
     * Test of list method, of class LoggerController.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testList() throws Exception {
        log.info("list");
        mvc.perform(get(logs))
                .andExpect(status().isOk()).andDo(print())
                .andExpect(jsonPath("$._links.self.href").isString())
                .andExpect(jsonPath("$._embedded.loggerDToes").isArray())
                .andExpect(jsonPath("$.page.size").value(restConfig.getDefaultPageSize()))
                .andDo(print());
    }

    @Test
    public void testList2() throws Exception {
        int pageSize = 2;
        mvc.perform(get(logs).param("size", String.valueOf(pageSize)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.page.size").value(pageSize));
    }

    /**
     * Test of changeLevel method, of class LoggerController.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testChangeLevel() throws Exception {
        log.info("changeLevel");
        String name = "ROOT";
        String json = "{\"name\":\"%s\",\"level\":\"%s\"}";
        Level level = loggers.findOne(name).get().getLevel();
        try {
            // new level different from original level
            Level newLevel = level == Level.WARN ? Level.ALL : Level.WARN;
            mvc.perform(
                    post(logs)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(String.format(json, name, newLevel))
            )
                    .andExpect(status().is2xxSuccessful());
            assertEquals(newLevel, loggers.findOne(name).get().getLevel());
        } finally {
            loggers.findOne(name).get().setLevel(level);
        }
    }

    @Test
    public void testListAsXml() throws Exception {
        mvc.perform(get(logs).accept(APPLICATION_XML))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_XML))
                .andDo(print());
    }

}
