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
package com.github.zhanhb.judge.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 *
 * @author zhanhb
 */
public class Calendars {

    public static GregorianCalendar toCalendar(LocalDateTime datetime) {
        return datetime == null ? null
                : GregorianCalendar.from(datetime.atZone(ZoneId.systemDefault()));
    }

    public static Date toDate(LocalDateTime datetime) {
        try {
            return datetime == null ? null
                    : new Date(datetime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
        } catch (ArithmeticException ex) {
            throw new IllegalArgumentException(ex);
        }
    }

    private Calendars() {
    }

}
