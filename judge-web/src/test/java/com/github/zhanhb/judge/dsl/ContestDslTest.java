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
import static com.github.zhanhb.judge.dsl.ContestDsl.ended;
import static com.github.zhanhb.judge.dsl.ContestDsl.running;
import static com.github.zhanhb.judge.dsl.ContestDsl.scheduling;
import com.github.zhanhb.judge.repository.ContestRepository;
import static com.google.common.collect.Iterables.size;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import lombok.extern.slf4j.Slf4j;
import static org.junit.Assert.assertEquals;
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

    /**
     * Test of scheduling method, of class ContestSpec.
     */
    @Test
    public void testFindAll() {
        LocalDateTime start = LocalDateTime.now().minusSeconds(5);
        int schedulingSize = size(contests.findAll(scheduling()));
        int runningSize = size(contests.findAll(running()));
        int endedSize = size(contests.findAll(ended()));

        long id = contests.save(Contest
                .builder()
                .beginTime(start)
                .finishTime(start.plus(5, ChronoUnit.HOURS))
                .name("test_contest")
                .title("title")
                .type(ContestType.CONTEST)
                .build()
        ).getId();

        try {
            Iterable<Contest> scheduling = contests.findAll(scheduling());
            assertEquals(schedulingSize, size(scheduling));
            Iterable<Contest> running = contests.findAll(running());
            assertEquals(runningSize + 1, size(running));
            Iterable<Contest> ended = contests.findAll(ended());
            assertEquals(endedSize, size(ended));
        } finally {
            contests.delete(id);
        }
    }
}
