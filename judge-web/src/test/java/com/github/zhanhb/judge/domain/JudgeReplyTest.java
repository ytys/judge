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

import java.util.Arrays;
import static java.util.stream.Collectors.toList;
import lombok.extern.slf4j.Slf4j;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import org.junit.Test;

/**
 *
 * @author zhanhb
 */
@Slf4j
public class JudgeReplyTest {

    @Test
    public void testAccepted() {
        assertThat(JudgeReply.Accepted.ordinal(), is(6));
    }

    @Test
    public void testToString() {
        log.debug("{}", Arrays.stream(JudgeReply.values()).collect(toList()));
    }

}
