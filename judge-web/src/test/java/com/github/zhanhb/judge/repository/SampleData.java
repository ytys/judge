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

import com.github.zhanhb.judge.domain.Contest;
import com.github.zhanhb.judge.domain.ContestType;
import com.github.zhanhb.judge.domain.Language;
import com.github.zhanhb.judge.domain.Problem;
import com.github.zhanhb.judge.domain.Userprofile;
import java.time.LocalDateTime;
import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author zhanhb
 */
@Component
public class SampleData {

    @Resource
    private UserprofileRepository userprofiles;
    @Autowired
    private ContestRepository contests;
    @Autowired
    private SubmissionRepository submissions;
    @Autowired
    private LanguageRepository languages;
    @Autowired
    private ProblemRepository problems;

    public Problem problem() {
        return problems.save(Problem.builder()
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
    }

    public Contest contest() {
        String contestName = "test_contest_name";

        return contests.save(Contest.builder()
                .beginTime(LocalDateTime.now())
                .finishTime(LocalDateTime.now())
                .type(ContestType.OJ)
                .title("title")
                .name(contestName)
                .build());
    }

    public Userprofile userprofile() {
        return userprofiles.save(Userprofile.builder().handle("test_user1").build());
    }

    public Language language() {
        return languages.save(Language.builder().name("testLanguage").executableExtension("tmp").languageExtension("tmp").build());
    }

    void delete(Userprofile userprofile) {
        userprofiles.delete(userprofile);
    }

    void delete(Language language) {
        languages.delete(language);
    }

    void delete(Contest contest) {
        contests.delete(contest);
    }

    void delete(Problem problem) {
        problems.delete(problem);
    }

}
