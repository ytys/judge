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
package com.github.zhanhb.judge;

import com.github.zhanhb.judge.testenv.AbstractContextControllerTests;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/**
 *
 * @author zhanhb
 */
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class LocaleTest extends AbstractContextControllerTests {

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        mockMvc = webAppContextSetup(wac)
                .alwaysExpect(status().isOk())
                // TODO
                .alwaysExpect(cookie().path("locale", "/"))
                .build();
    }

    @Test
    public void testChangeLocale() throws Exception {org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration b;
        mockMvc.perform(get("/?lang=en")).andExpect(cookie().value("locale", "en"));
        mockMvc.perform(get("/?lang=zh")).andExpect(cookie().value("locale", "zh"));
    }

    @Ignore
    @Test
    public void testChangeLocaleDash() throws Exception {
        mockMvc.perform(get("/?lang=en-US")).andExpect(cookie().value("locale", "en_US"));
    }
}
