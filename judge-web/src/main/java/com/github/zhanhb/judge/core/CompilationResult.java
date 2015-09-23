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
package com.github.zhanhb.judge.core;

import java.io.Serializable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author zhanhb
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Setter(AccessLevel.PRIVATE)
public class CompilationResult implements Serializable {

    private static final long serialVersionUID = 1L;
    public static final CompilationResult SUCCESS = new CompilationResult();

    private boolean pass;
    private String message;
    private String detailMessage;

    public CompilationResult success(String message) {
        return success(message, null);
    }

    public CompilationResult success(String message, String detailMessage) {
        return new CompilationResult(true, message, detailMessage);
    }

    public CompilationResult fail(String message) {
        return fail(message, null);
    }

    public CompilationResult fail(String message, String detailMessage) {
        return new CompilationResult(false, message, detailMessage);
    }

}
