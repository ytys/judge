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
package com.github.zhanhb.judge.main;

import com.github.zhanhb.judge.util.MatcherWrapper;
import com.github.zhanhb.judge.util.Strings;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Pattern;

/**
 *
 * @author zhanhb
 */
public class JSEncoder {

    public static void main(String[] args) throws IOException {
        Path path = Paths.get("src/main/webapp/static/bootstrap-table/bootstrap-table-locale-zh.js");
        byte[] bytes = Files.readAllBytes(path);
        String str = StandardCharsets.UTF_8.newDecoder().decode(ByteBuffer.wrap(bytes)).toString();
        str = new MatcherWrapper(Pattern.compile("[\u007f-\uffff]"), str).replaceAll(matcher
                -> "\\u" + Strings.slice(Integer.toHexString(matcher.group().charAt(0)), -4));

        Files.write(path, str.getBytes());
    }

}
