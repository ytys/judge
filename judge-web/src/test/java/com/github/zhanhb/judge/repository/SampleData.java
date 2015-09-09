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
import com.github.zhanhb.judge.domain.Submission;
import com.github.zhanhb.judge.domain.Userprofile;
import com.github.zhanhb.judge.util.SecurityUtils;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 *
 * @author zhanhb
 */
@Component
public class SampleData {

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
    private RepositoryRestConfiguration restConfig;

    public void problem(Consumer<Problem> consumer) {
        Objects.requireNonNull(consumer);
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
        try {
            consumer.accept(problem);
        } finally {
            problems.delete(problem);
        }
    }

    public void contest(Consumer<Contest> consumer) {
        Objects.requireNonNull(consumer);
        contest(builder -> builder.beginTime(LocalDateTime.now())
                .finishTime(LocalDateTime.now())
                .type(ContestType.OJ)
                .title("title")
                .name("test_contest_name"), consumer);
    }

    public void contest(UnaryOperator<Contest.ContestBuilder> operator, Consumer<Contest> consumer) {
        Objects.requireNonNull(consumer);
        Contest contest = contests.save(operator.apply(Contest.builder()).build());
        try {
            consumer.accept(contest);
        } finally {
            contests.delete(contest);
        }
    }

    public void userprofile(Consumer<Userprofile> consumer) {
        userprofile(builder -> builder.handle("test_user1").password("sample_password"), consumer);
    }

    public void language(Consumer<Language> consumer) {
        language(builder -> builder
                .name("testLanguage")
                .executableExtension("tmp")
                .languageExtension("tmp"), consumer);
    }

    public void submission(Consumer<Submission> consumer) {
        problem(problem -> language(language -> userprofile(userprofile -> contest(contest -> {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            try {
                SecurityUtils.runAs(userprofile.getHandle(), userprofile.getPassword());
                Submission submission = submissions.save(Submission.builder()
                        .contest(contest)
                        .userprofile(userprofile)
                        .language(language)
                        .problem(problem)
                        .build());
                try {
                    consumer.accept(submission);
                } finally {
                    submissions.delete(submission);
                }
            } finally {
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }))));
    }

    public void language(UnaryOperator<Language.LanguageBuilder> func, Consumer<Language> consumer) {
        Objects.requireNonNull(consumer);
        Language g = languages.save(func.apply(Language.builder()).build());
        try {
            consumer.accept(g);
        } finally {
            languages.delete(g);
        }
    }

    public void userprofile(UnaryOperator<Userprofile.UserprofileBuilder> func, Consumer<Userprofile> consumer) {
        Objects.requireNonNull(consumer);
        Userprofile u = userprofiles.save(func.apply(Userprofile.builder()).build());
        try {
            consumer.accept(u);
        } finally {
            userprofiles.delete(u);
        }
    }

    public Pageable pageable() {
        return new PageRequest(0, restConfig.getDefaultPageSize());
    }
}
