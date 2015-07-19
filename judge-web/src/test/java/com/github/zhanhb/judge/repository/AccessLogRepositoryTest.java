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
import com.github.zhanhb.judge.model.AccessLog;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author zhanhb
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class AccessLogRepositoryTest {

    @Autowired
    private AccessLogRepository accessLogRepository;

    @Test
    @Transactional
    public void testSomeMethod() {
        for (String string : new String[]{"/", "/faq", "/login", "/problem/list", "/problem/1"}) {
            AccessLog accessLog = accessLogRepository.save(AccessLog.builder()
                    .behaviour("test")
                    .ip("127.0.0.1")
                    .url(string)
                    .build());
            assertNotNull(accessLog.getAccessTime());
            assertTrue(accessLogRepository.findById(accessLog.getId()).get() == accessLog);
        }
    }
}
