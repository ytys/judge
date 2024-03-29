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
import com.github.zhanhb.judge.domain.AccessLog;
import lombok.extern.slf4j.Slf4j;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author zhanhb
 */
@RunWith(SpringJUnit4ClassRunner.class)
@Slf4j
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class AccessLogRepositoryTest {

    @Autowired
    private AccessLogRepository accessLogs;
    @Autowired
    private SampleData sampleData;

    @Test
    @Transactional
    public void testSomeMethod() {
        log.info("someMethod");
        for (String string : new String[]{"/", "/faq", "/login", "/problem/list", "/problem/1"}) {
            AccessLog accessLog = accessLogs.save(AccessLog.builder()
                    .behaviour("test")
                    .ip("127.0.0.1")
                    .url(string)
                    .build());
            assertNotNull(accessLog.getAccessTime());
            assertThat(accessLogs.findOne(accessLog.getId()).get(), is(accessLog));
        }
    }

    /**
     * Test of findAllByUserprofileHandleIgnoreCase method, of class
     * AccessLogRepository.
     */
    @Test
    public void testFindAllByUserprofileHandleIgnoreCase() {
        log.info("findAllByUserprofileHandleIgnoreCase");
        String userprofileHandle = "admin";
        accessLogs.findAllByUserprofileHandleIgnoreCase(userprofileHandle, sampleData.pageable());
    }

    /**
     * Test of findByUserprofile method, of class AccessLogRepository.
     */
    @Test
    public void testFindByUserprofile() {
        log.info("findByUserprofile");
        sampleData.userprofile(userprofile -> accessLogs.findByUserprofile(userprofile, sampleData.pageable()));
    }

}
