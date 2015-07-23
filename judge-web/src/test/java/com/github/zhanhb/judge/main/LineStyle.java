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

import com.github.zhanhb.judge.util.Strings;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UncheckedIOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 *
 * @author zhanhb
 */
public class LineStyle {

    private static final String LINE_STYLE;
    private static final Set<String> acceptsuffix = new HashSet<>(Arrays.asList("java,xml,jspx,tagx,css,js,jsp,tag,yml".split("[^a-z]+")));

    static {
        StringWriter sw = new StringWriter(4);
        PrintWriter pw = new PrintWriter(sw);
        pw.println();
        pw.close();
        LINE_STYLE = sw.toString();
    }

    public static void main(String[] args) throws IOException {
        Files.walk(Paths.get("."))
                .filter(path -> accept(path))
                .filter(Files::isWritable)
                .forEach(path -> handle(path));
    }

    private static boolean accept(Path path) {
        return acceptsuffix.contains(getExtension(path.toString()));
    }

    private static void handle0(Path path) throws IOException {
        long lastModified = Files.getLastModifiedTime(path).toMillis();
        Charset charset = StandardCharsets.ISO_8859_1;
        try {
            String str = Files.readAllLines(path, charset).stream().map(Strings::trimRight)
                    .collect(Collectors.joining(LINE_STYLE)) + LINE_STYLE;
            Files.write(path, str.getBytes(charset));
        } finally {
            // add one second
            Files.setLastModifiedTime(path, FileTime.fromMillis(lastModified + 1000));
        }
    }

    private static void handle(Path path) {
        try {
            handle0(path);
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }

    @SuppressWarnings("AssignmentToMethodParameter")
    private static String getExtension(String filename) {
        int indexOf = filename.indexOf(File.separatorChar);
        if (indexOf >= 0) {
            filename = filename.substring(indexOf);
        }
        if (File.separatorChar != '/') {
            indexOf = filename.lastIndexOf('/');
            if (indexOf >= 0) {
                filename = filename.substring(indexOf);
            }
        }
        int lastIndexOf = filename.lastIndexOf('.');
        // a file whose name has only extension
        // such as .gitignore is regard as no extension
        if (lastIndexOf > 0) {
            return filename.substring(lastIndexOf + 1);
        }
        return "";
    }
}
