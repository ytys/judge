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
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import static org.junit.Assert.assertEquals;
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
@Slf4j
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class ContestDslTest {

    @Autowired
    private ContestRepository repository;

    /**
     * Test of scheduling method, of class ContestSpec.
     */
    @Test(expected = Roolback.class)
    @Transactional(rollbackFor = Roolback.class)
    public void testFindAll() {
        LocalDateTime start = LocalDateTime.now().minusSeconds(5);
        int schedulingSize = repository.findAll(scheduling()).size();
        int runningSize = repository.findAll(running()).size();
        int endedSize = repository.findAll(ended()).size();

        long id = repository.save(Contest
                .builder()
                .beginTime(start)
                .finishTime(start.plus(5, ChronoUnit.HOURS))
                .title("title")
                .type(ContestType.CONTEST)
                .build()
        ).getId();

        log.debug(String.valueOf(id));

        log.info("scheduling");
        List<Contest> scheduling = repository.findAll(scheduling());
        log.info("scheduling = " + scheduling);
        List<Contest> running = repository.findAll(running());
        assertEquals(schedulingSize, scheduling.size());
        log.info("running = " + running);
        assertEquals(runningSize + 1, running.size());
        List<Contest> ended = repository.findAll(ended());
        log.info("ended = " + ended);
        assertEquals(endedSize, ended.size());
        throw new Roolback();
    }

    // just use to roolback the transaction
    private static class Roolback extends RuntimeException {

        private static final long serialVersionUID = 3521399605135440155L;
    }
}
