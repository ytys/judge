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
package com.github.zhanhb.judge.service;

import com.github.zhanhb.judge.domain.JudgeReply;
import com.github.zhanhb.judge.domain.Submission;
import com.github.zhanhb.judge.repository.SubmissionRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author zhanhb
 */
@Service
public class JudgeService {

    @Autowired
    private SubmissionRepository submissions;

    @Transactional(readOnly = true)
    public Optional<Submission> findOne(long id) {
        return submissions.findOne(id);
    }

    @Transactional
    public void updataStatus(Submission submission, JudgeReply judgeReply) {
        submission.setJudgeReply(judgeReply);
        submissions.save(submission);
    }

}
