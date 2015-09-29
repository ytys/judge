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

import com.github.zhanhb.judge.Application;
import lombok.extern.slf4j.Slf4j;
import static org.junit.Assert.assertTrue;
import org.junit.AssumptionViolatedException;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 *
 * @author zhanhb
 */
@DirtiesContext
// security reason
@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@Slf4j
@SpringApplicationConfiguration(classes = Application.class)
@WebIntegrationTest(value = "server.context-path=" + H2ConsoleConfigurationTest.CONTEXT_PATH, randomPort = true)
public class H2ConsoleConfigurationTest {

    static final String CONTEXT_PATH = "/znoj";

    @Autowired
    private ApplicationContext context;
    @Value("http://localhost:${local.server.port:0}" + CONTEXT_PATH)
    private String baseUrl;

    /**
     * Test of h2Console method, of class H2ConsoleConfiguration. It will be
     * tested when spring-boot.version is greater or equal than 1.3.0.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testH2Console() throws Exception {
        String className = "org.springframework.boot.autoconfigure.h2.H2ConsoleProperties";
        Class<?> h2ConsolePropertiesClass;
        try {
            h2ConsolePropertiesClass = Class.forName(className);
        } catch (ClassNotFoundException ex) {
            throw new AssumptionViolatedException("spring-boot.version less than 1.3.0, test skipped");
        }

        Object h2ConsoleProperties = context.getBean(h2ConsolePropertiesClass);
        String path = (String) h2ConsolePropertiesClass.getMethod("getPath").invoke(h2ConsoleProperties);
        String url = baseUrl + path + (path.endsWith("/") ? "" : "/");
        log.debug(url);
        ResponseEntity<String> entity = new TestRestTemplate().getForEntity(url, String.class);
        // status code is either success or redirection
        // not client error or server error
        HttpStatus.Series series = entity.getStatusCode().series();

        assertTrue("wrong status code " + entity.getStatusCode() + " for request '" + url + "'",
                series == HttpStatus.Series.REDIRECTION
                || series == HttpStatus.Series.SUCCESSFUL);

    }

}
