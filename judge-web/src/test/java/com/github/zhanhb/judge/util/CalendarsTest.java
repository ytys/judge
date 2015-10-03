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
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import lombok.extern.slf4j.Slf4j;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.lessThan;
import static org.junit.Assert.assertThat;
import org.junit.Test;

/**
 *
 * @author zhanhb
 */
@Slf4j
public class CalendarsTest {

    /**
     * Test of toCalendar method, of class Calendars.
     */
    @Test
    public void testToCalendar() {
        log.info("toCalendar");
        LocalDateTime datetime = LocalDateTime.now();
        Calendar expResult = Calendar.getInstance();
        GregorianCalendar result = Calendars.toCalendar(datetime);
        assertThat(result.getTimeInMillis() - expResult.getTimeInMillis(), allOf(greaterThan(-1000L), lessThan(1000L)));
    }

    /**
     * Test of toDate method, of class Calendars.
     */
    @Test
    public void testToDate() {
        log.info("toDate");
        LocalDateTime datetime = LocalDateTime.now();
        Date expResult = new Date();
        Date result = Calendars.toDate(datetime);
        assertThat(result.getTime() - expResult.getTime(), allOf(greaterThan(-1000L), lessThan(1000L)));
    }

    @Test
    public void testConstructor() throws Exception {
        TestUtils.testConstructor(Calendars.class);
    }

}
