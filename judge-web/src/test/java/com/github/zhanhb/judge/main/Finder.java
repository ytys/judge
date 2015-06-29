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

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author zhanhb
 * @date May 31, 2015, 23:23:44
 */
@Slf4j
public class Finder {

    public static void main(String[] args) throws Throwable {
        log.info("stream.count() = " + getStream().count());
        String str = getStream().map(Object::toString).collect(Collectors.joining("\n"));
        log.info(str);
    }

    private static Stream<Path> getStream() throws Throwable {
        return Files.walk(Paths.get("src"))
                .filter(Files::isRegularFile)
                .filter(path -> path.toString().matches("(?i).+\\.(?:jsp|tag)$"));
    }
}
