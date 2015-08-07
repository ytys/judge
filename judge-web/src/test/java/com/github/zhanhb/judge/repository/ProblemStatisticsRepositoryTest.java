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
import com.github.zhanhb.judge.domain.Problem;
import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
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
public class ProblemStatisticsRepositoryTest {

    @Autowired
    private ProblemStatisticsRepository repository;
    @Autowired
    private ProblemRepository problems;

    @Test
    public void test() {
        Problem problem = problems.save(Problem.builder()
                .lastUpdateDate(LocalDateTime.now())
                .creationDate(LocalDateTime.now())
                .source("source")
                .hint("hint")
                .input("input")
                .output("output")
                .sampleInput("sample input")
                .sampleOutput("sample output")
                .description("desc")
                .title("title")
                .build());
        assertThat(problems.findOne(problem.getId()).get(), is(problem));
        assertTrue("problem " + problem.getId() + " not exists", repository.findOne(problem.getId()).isPresent());
        problems.delete(problem);
    }

}
