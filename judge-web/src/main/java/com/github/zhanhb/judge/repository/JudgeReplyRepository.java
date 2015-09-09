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

import com.github.zhanhb.judge.domain.JudgeReply;
import java.util.Optional;

/**
 *
 * @author zhanhb
 */
public interface JudgeReplyRepository extends BaseRepository<JudgeReply, Long> {

    Optional<JudgeReply> findByName(String string);

    default JudgeReply findByNameOrCreate(String name) {
        return findByName(name).orElseGet(() -> save(JudgeReply.builder().name(name).build()));
    }

    default JudgeReply internalError() {
        return findByNameOrCreate(Constants.JUDGE_INTERNAL_ERROR);
    }

    default JudgeReply queueing() {
        return findByNameOrCreate(Constants.QUEUEING);
    }

    default JudgeReply compiling() {
        return findByNameOrCreate(Constants.COMPILING);
    }

    default JudgeReply running() {
        return findByNameOrCreate(Constants.RUNNING);
    }

}
