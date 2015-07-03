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

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

/**
 *
 * @author zhanhb
 * @date May 30, 2015, 16:14:06
 */
public class ReadOnly {

    public static void main(String[] args) throws IOException {
        Files.walkFileTree(Paths.get("src/main/webapp/static/bootstrap"), new SetReadOnlyVisitor());
        Files.walkFileTree(Paths.get("src/main/webapp/static/bootstrap-table"), new SetReadOnlyVisitor());
        Files.walkFileTree(Paths.get("src/main/webapp/static/ckeditor"), new SetReadOnlyVisitor());
        Files.walkFileTree(Paths.get("src/main/webapp/static/css/problem-statement.css"), new SetReadOnlyVisitor());
        Files.walkFileTree(Paths.get("src/main/webapp/static/commons/js"), new SetReadOnlyVisitor());
    }

    private static class SetReadOnlyVisitor extends SimpleFileVisitor<Path> {

        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
            file.toFile().setWritable(false);
            return super.visitFile(file, attrs);
        }
    }
}
