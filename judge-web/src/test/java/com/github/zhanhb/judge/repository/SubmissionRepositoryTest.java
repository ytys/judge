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
import com.github.zhanhb.judge.domain.Contest;
import com.github.zhanhb.judge.domain.Language;
import com.github.zhanhb.judge.domain.Problem;
import com.github.zhanhb.judge.domain.Submission;
import com.github.zhanhb.judge.domain.Userprofile;
import lombok.extern.slf4j.Slf4j;
import static org.junit.Assert.assertNotEquals;
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
    private SubmissionRepository submissions;
    @Autowired
    private ContestRepository contests;
    @Autowired
    private SampleData sampleData;

    @Test
    public void testFindAllByContestName() {
        log.info("findAllByContestName");

        Contest contest = sampleData.contest();
        Userprofile userprofile = sampleData.userprofile();
        Language language = sampleData.language();
        Problem problem = sampleData.problem();
        String contestName = contest.getName();

        Submission submission = submissions.save(Submission.builder()
                .contest(contest)
                .userprofile(userprofile)
                .language(language)
                .problem(problem)
                .build());
        Pageable pageable = new PageRequest(0, 20);
        Page<Submission> page = submissions.findAllByContestName(contestName, pageable);
        assertNotEquals(0, page.getTotalElements());
        log.debug("{}", page);
        submissions.delete(submission);
        sampleData.delete(problem);
        sampleData.delete(language);
        sampleData.delete(userprofile);
        sampleData.delete(contest);
    }

}
