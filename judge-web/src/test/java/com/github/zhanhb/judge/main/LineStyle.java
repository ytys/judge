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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.io.UncheckedIOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.util.StringUtils;

/**
 *
 * @author zhanhb
 */
@Slf4j
public class LineStyle {

    private static final String LINE_STYLE = "\n";
    private static final Set<String> ACCEPT_SUFFIX = new HashSet<>(Arrays.asList("java,xml,jspx,tagx,css,js,jsp,tag,yml,properties,sql,txt,html,txt".split("[^a-z]+")));

    public static void main(String[] args) throws IOException {
        Files.walk(Paths.get("."))
                .filter(LineStyle::accept)
                .filter(Files::isWritable)
                .forEach(LineStyle::handle);
    }

    private static boolean accept(Path path) {
        return ACCEPT_SUFFIX.contains(FilenameUtils.getExtension(path.toString()));
    }

    private static void handle0(Path path) throws IOException {
        Charset charset = StandardCharsets.ISO_8859_1;
        byte[] bytes = Files.readAllBytes(path);
        String content = charset.newDecoder().decode(ByteBuffer.wrap(bytes)).toString();
        String str = new BufferedReader(new StringReader(content)).lines().map(StringUtils::trimTrailingWhitespace)
                .collect(Collectors.joining(LINE_STYLE)) + LINE_STYLE;

        if (!str.equals(content)) {
            log.info("{}", path);
            Files.write(path, str.getBytes(charset));
        }
    }

    private static void handle(Path path) {
        try {
            handle0(path);
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }

}
