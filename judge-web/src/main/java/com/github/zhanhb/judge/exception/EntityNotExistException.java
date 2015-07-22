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
package com.github.zhanhb.judge.exception;

import com.github.zhanhb.judge.util.StringUtils;

/**
 *
 * @author zhanhb
 */
public class EntityNotExistException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private static String defaultMessage() {
        return "can't find the entity";
    }

    public EntityNotExistException() {
        super(defaultMessage());
    }

    public EntityNotExistException(String message) {
        super(StringUtils.isEmpty(message) ? defaultMessage() : message);
    }

    @Override
    public Throwable fillInStackTrace() {
        return this;
    }

}
