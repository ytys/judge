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
package com.github.zhanhb.judge.dsl;

import com.github.zhanhb.judge.Application;
import com.github.zhanhb.judge.domain.Contest;
import com.github.zhanhb.judge.domain.ContestType;
import com.github.zhanhb.judge.repository.ContestRepository;
import com.github.zhanhb.judge.repository.SampleData;
import com.github.zhanhb.judge.util.TestUtils;
import com.google.common.collect.Iterables;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import lombok.extern.slf4j.Slf4j;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 *
 * @author zhanhb
 */
@RunWith(SpringJUnit4ClassRunner.class)
@Slf4j
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class ContestDslTest {

    @Autowired
    private ContestRepository contests;
    @Autowired
    private SampleData sampleData;

    /**
     * Test of querydsl methods, of class ContestSpec.
     */
    @Test
    public void testQuerydsl() {
        LocalDateTime start = LocalDateTime.now().minusSeconds(5);
        LocalDateTime end = start.plus(5, ChronoUnit.HOURS);
        int schedulingSize = Iterables.size(contests.findAll(ContestDsl.scheduling()));
        int runningSize = Iterables.size(contests.findAll(ContestDsl.running()));
        int endedSize = Iterables.size(contests.findAll(ContestDsl.ended()));

        sampleData.contest(builder -> builder.beginTime(start)
                .finishTime(end)
                .name("test_contest")
                .title("title")
                .type(ContestType.CONTEST), contest -> {
            Iterable<Contest> scheduling = contests.findAll(ContestDsl.scheduling());
            Iterable<Contest> running = contests.findAll(ContestDsl.running());
            Iterable<Contest> ended = contests.findAll(ContestDsl.ended());

            assertEquals(schedulingSize, Iterables.size(scheduling));
            assertEquals(runningSize + 1, Iterables.size(running));
            assertEquals(endedSize, Iterables.size(ended));

            assertThat(running, contains(contest));
            assertThat(scheduling, not(contains(contest)));
            assertThat(ended, not(contains(contest)));
        });
    }

    @Test
    public void testConstructor() throws Exception {
        TestUtils.testConstructor(ContestDsl.class);
    }

}
