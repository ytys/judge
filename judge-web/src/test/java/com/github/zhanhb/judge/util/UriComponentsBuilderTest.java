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

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.springframework.web.util.UriComponentsBuilder;

/**
 *
 * @author zhanhb
 */
public class UriComponentsBuilderTest {

    private String encode(String path, String param) {
        return UriComponentsBuilder.fromPath(path)
                .build()
                .expand(param)
                .encode().toUriString();
    }

    @Test
    public void hello() {
        String hello = "\u4f60\u597d";
        String result = encode("/user/search/{userHandle}", hello);
        String expectResult = "/user/search/%E4%BD%A0%E5%A5%BD";
        assertEquals(expectResult, result);
    }

}
