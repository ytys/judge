/*
 * Copyright 2015 Pivotal Software, Inc..
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
import com.github.zhanhb.judge.domain.Contest;
import com.github.zhanhb.judge.domain.Submission;
import com.github.zhanhb.judge.domain.ContestType;
import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
public class SubmissionRepositoryTest {

    @Autowired
    private SubmissionRepository repository;
    @Autowired
    private ContestRepository cr;

    @Test
    public void testFindAllByContestName() {
        log.info("findAllByContestName");
        String contestName = "test_contest_name";

        Contest contest = cr.save(Contest.builder()
                .beginTime(LocalDateTime.now())
                .finishTime(LocalDateTime.now())
                .type(ContestType.OJ)
                .title("title")
                .name(contestName)
                .build());

        Submission.builder().contest(contest);
        Pageable pageable = new PageRequest(0, 20);
        Page<Submission> page = repository.findAllByContestName(contestName, pageable);
        log.debug("{}", page);
        cr.delete(contest);
    }

}
