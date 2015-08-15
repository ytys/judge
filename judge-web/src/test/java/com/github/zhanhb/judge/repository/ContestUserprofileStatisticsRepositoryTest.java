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
import com.github.zhanhb.judge.domain.ContestUserprofileStatistics;
import com.github.zhanhb.judge.domain.Language;
import com.github.zhanhb.judge.domain.Problem;
import com.github.zhanhb.judge.domain.Submission;
import com.github.zhanhb.judge.domain.Userprofile;
import java.util.Collection;
import javax.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.not;
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
public class ContestUserprofileStatisticsRepositoryTest {

    @Autowired
    private UserprofileRepository userprofiles;
    @Autowired
    private ContestRepository contests;
    @Autowired
    private SubmissionRepository submissions;
    @Autowired
    private LanguageRepository languages;
    @Autowired
    private ProblemRepository problems;
    @Autowired
    private SampleData sampleData;
    @Autowired
    private ContestUserprofileStatisticsRepository contestUserprofileStatisticss;

    @Test
    @Transactional
    public void test() {
        Problem problem = sampleData.problem();
        Language language = sampleData.language();
        Userprofile userprofile = sampleData.userprofile();
        Contest contest = sampleData.contest();

        Submission submission = submissions.save(Submission.builder()
                .contest(contest)
                .userprofile(userprofile)
                .language(language)
                .problem(problem)
                .build());

        Iterable<ContestUserprofileStatistics> list = contestUserprofileStatisticss.findAll();

        assertThat((Collection<?>)list, not(hasSize(0)));

        submissions.delete(submission);
        contests.delete(contest);
        userprofiles.delete(userprofile);
        languages.delete(language);
        problems.delete(problem);
    }

}
