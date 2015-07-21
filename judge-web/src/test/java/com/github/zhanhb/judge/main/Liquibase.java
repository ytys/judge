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
import com.github.zhanhb.judge.util.StringUtils;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Pattern;

/**
 *
 * @author zhanhb
 */
public class Liquibase {

    public static void main(String[] args) throws IOException, URISyntaxException {
        String resource = "config/liquibase/changelog/20150721174338_changelog.xml";
        Path initSchema = Paths.get("src/main/resources", resource);
        String[][] replaces = {
            {"author=\"[^\"]+\"", "author=\"system\""},
            {"(?<prefix><addForeignKeyConstraint[^>]+?\")(?<space>\\s*)(?<suffix>referencedColumnNames=[^>]+?/>)", "${prefix} onUpdate=\"CASCADE\" ${suffix}"}
        };
        Charset charset = StandardCharsets.ISO_8859_1;
        String str = new String(Files.readAllBytes(initSchema), charset);
        for (String[] replace : replaces) {
            str = str.replaceAll(replace[0], replace[1]);
        }
        str = new MatcherWrapper(Pattern.compile("id=\"\\d+-(\\d+)\""), str).replaceAll(matcher -> {
            String t = StringUtils.slice("0000000000000" + matcher.group(1), -14);
            return "id=\"" + t + "\"";
        });
        Files.write(initSchema, str.getBytes(charset));
    }

}
