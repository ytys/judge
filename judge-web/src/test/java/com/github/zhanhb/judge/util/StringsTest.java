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

import java.util.Random;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import lombok.extern.slf4j.Slf4j;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author zhanhb
 */
@Slf4j
public class StringsTest {

    private StringBuilder merge(StringBuilder a, StringBuilder b) {
        throw new IllegalStateException();
    }

    /**
     * Test of slice method, of class StringUtils.
     *
     * @throws javax.script.ScriptException
     */
    @Test
    public void testSlice_String_int() throws ScriptException {
        log.info("slice");
        Random random = new Random();
        ScriptEngine javascript = new ScriptEngineManager().getEngineByName("javascript");

        int length = random.nextInt(20) + 5;
        for (int i = 0; i < 30; ++i) {
            String randomString = random.ints(length, 'a', 'z' + 1)
                    .collect(StringBuilder::new,
                            (sb, x) -> sb.append((char) x),
                            this::merge).toString();
            assertEquals(length, randomString.length());

            for (int j = 0; j < 30; ++j) {
                int start = random.nextInt(length * 6) - length * 3;
                String result = Strings.slice(randomString, start);

                String expResult = (String) javascript.eval("\'" + randomString + "\'.slice(" + start + ")");
                assertEquals(expResult, result);
            }
        }
    }

    /**
     * Test of slice method, of class StringUtils.
     *
     * @throws javax.script.ScriptException
     */
    @Test
    public void testSlice_3args() throws ScriptException {
        log.info("slice");
        Random random = new Random();
        ScriptEngine javascript = new ScriptEngineManager().getEngineByName("javascript");

        int length = random.nextInt(20) + 5;
        for (int i = 0; i < 30; ++i) {
            String randomString = random.ints(length, 'a', 'z' + 1)
                    .collect(StringBuilder::new,
                            (sb, x) -> sb.append((char) x),
                            this::merge).toString();
            assertEquals(length, randomString.length());

            for (int j = 0; j < 30; ++j) {
                int start = random.nextInt(length * 6) - length * 3;
                int end = random.nextInt(length * 6) - length * 3;
                String result = Strings.slice(randomString, start, end);

                String expResult = (String) javascript.eval("\'" + randomString + "\'.slice(" + start + "," + end + ")");
                assertEquals(expResult, result);
            }
        }
    }

}