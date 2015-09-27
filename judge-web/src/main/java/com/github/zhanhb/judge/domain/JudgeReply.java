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
    queuing,
    compiling,
    running,
    runtimeError,
    wrongAnswer,
    accepted,
    timeLimitExceeded,
    memoryLimitExceeded,
    outOfContestTime,
    restrictedFunction,
    outputLimitExceeded,
    noSuchProblem,
    compilationError,
    presentationError,
    judgeInternalError,
    floatingPointError,
    segmentationFault,
    prepareCompilation,
    prepareExecution,
    judging,
    submissionLimitExceeded,
    aborted;

    private static final JudgeReply[] values = values();
    private transient final String toString = toString(name());

    @Override
    public String toString() {
        return toString;
    }

    public static String toString(String name) {
        char charAt0 = name.charAt(0);
        return charAt0 == '_' ? name
                : (Character.toUpperCase(charAt0)
                + name.substring(1).replaceAll("[A-Z]", " $0"))
                .replace(" Of ", " of ");
    }

    /**
     * Returns the enum constant of this type with the specified name. The
     * string must match exactly an identifier used to declare an enum constant
     * in this type. (Extraneous whitespace characters are not permitted.)
     *
     * @param ordinal the ordinal of the enum constant to be returned.
     * @return the enum constant with the specified name
     * @throws IllegalArgumentException if this enum type has no constant with
     * the specified name
     */
    public static JudgeReply valueOf(int ordinal) {
        try {
            return values[ordinal];
        } catch (ArrayIndexOutOfBoundsException ex) {
            throw new IllegalArgumentException();
        }
    }

}
