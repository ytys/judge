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
package com.github.zhanhb.judge.domain;

import java.io.Serializable;

/**
 *
 * @author zhanhb
 */
public enum JudgeReply implements Serializable {
    /* a placeholder whose ordinal is 0, do NOT use */
    @Deprecated
    __HOLDER__,
    Queuing,
    Compiling,
    Running,
    RuntimeError,
    WrongAnswer,
    Accepted,
    TimeLimitExceeded,
    MemoryLimitExceeded,
    OutOfContestTime {
        @Override
        public String toString() {
            return "Out of Contest Time";
        }
    },
    RestrictedFunction,
    OutputLimitExceeded,
    NoSuchProblem {
        @Override
        public String toString() {
            return "No such Problem";
        }
    },
    CompilationError,
    PresentationError,
    JudgeInternalError,
    FloatingPointError,
    SegmentationFault,
    PrepareCompilation,
    PrepareExecution,
    Judging,
    SubmissionLimitExceeded,
    Aborted;

    @Override
    public String toString() {
        return name().charAt(0) + name().substring(1).replaceAll("[A-Z]", " $0");
    }

}
