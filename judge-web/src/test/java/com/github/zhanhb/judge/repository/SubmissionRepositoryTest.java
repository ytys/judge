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
import com.github.zhanhb.judge.domain.Submission;
import lombok.extern.slf4j.Slf4j;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringApplicationConfiguration;
import org.springframework.data.domain.Page;
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
    private SubmissionRepository submissions;
    @Autowired
    private SampleData sampleData;

    @Test
    public void testFindAllByContestName() {
        log.info("findAllByContestName");

        sampleData.submission(submission -> {
            String contestName = submission.getContest().getName();
            Page<Submission> page = submissions.findAllByContestName(contestName, sampleData.pageable());
            assertThat(page.getTotalElements(), not(0));
            log.debug("{}", page);
        });
    }

    @Test
    public void testFindAll() {
        sampleData.submission(submission -> {
            assertThat(submissions.findAll(), contains(submission));
            assertThat(submissions.findOne(submission.getId()).get(), is(submission));
        });
    }

    @Test
    public void testForeignKeyNotNull() {
        sampleData.submission(submission -> {
            assertNotNull(submission.getContest());
            assertNotNull(submission.getProblem());
            assertNotNull(submission.getUserprofile());
        });
    }

}
