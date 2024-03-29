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

import com.github.zhanhb.judge.domain.QContest;
import com.querydsl.core.types.Predicate;
import java.time.LocalDateTime;

/**
 *
 * @author zhanhb
 */
public class ContestDsl {

    public static Predicate ended() {
        return QContest.contest.finishTime.lt(LocalDateTime.now());
    }

    public static Predicate scheduling() {
        return QContest.contest.beginTime.gt(LocalDateTime.now());
    }

    public static Predicate running() {
        LocalDateTime now = LocalDateTime.now();
        return QContest.contest.beginTime.lt(now).and(QContest.contest.finishTime.gt(now));
    }

    private ContestDsl() {
        throw new AssertionError();
    }

}
