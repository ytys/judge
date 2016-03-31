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

import lombok.extern.slf4j.Slf4j;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringApplicationConfiguration;
import org.springframework.boot.test.context.web.WebIntegrationTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 *
 * @author zhanhb
 */
@DirtiesContext
@Ignore("delete database")
@RunWith(SpringJUnit4ClassRunner.class)
@Slf4j
@SpringApplicationConfiguration(classes = Application.class)
@WebIntegrationTest(value = "server.context-path=" + LocaleTest.CONTEXT_PATH, randomPort = true)
public class LocaleTest {

    // TODO
    static final String CONTEXT_PATH = "/znoj";

    @Value("${local.server.port}")
    private int port;

    @Test
    public void testChangeLocale() throws Exception {
        String url = String.format("http://localhost:%d%s%s", port, CONTEXT_PATH, "/?lang=en");
        ResponseEntity<String> entity = new TestRestTemplate().getForEntity(url, String.class);
        String cookie = entity.getHeaders().getFirst("set-cookie");
        log.debug(entity.getBody());
        assertEquals(url, HttpStatus.OK, entity.getStatusCode());
        assertThat("cookie set error", cookie, containsString("locale=en"));

        url = String.format("http://localhost:%d%s%s", port, CONTEXT_PATH, "/?lang=zh");
        entity = new TestRestTemplate().getForEntity(url, String.class);
        cookie = entity.getHeaders().getFirst("set-cookie");
        assertEquals(url, HttpStatus.OK, entity.getStatusCode());
        assertThat("cookie set error", cookie, containsString("locale=zh"));
    }

    @Ignore
    @Test
    public void testChangeLocaleDash() throws Exception {
        String url = String.format("http://localhost:%d%s%s", port, CONTEXT_PATH, "/?lang=en_US");
        ResponseEntity<String> entity = new TestRestTemplate().getForEntity(url, String.class);
        String cookie = entity.getHeaders().getFirst("set-cookie");
        assertEquals(url, HttpStatus.OK, entity.getStatusCode());
        assertThat("cookie set error", cookie, containsString("locale=en_US"));
    }

}
