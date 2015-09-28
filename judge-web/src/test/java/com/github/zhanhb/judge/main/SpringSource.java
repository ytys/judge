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

import com.github.zhanhb.judge.util.DirectoryCopyHelper;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

/**
 *
 * @author zhanhb
 */
public class SpringSource {

    public static void main(String[] args) throws IOException {
        ArrayList<ClassLoader> list = new ArrayList<>(20);
        for (ClassLoader cl = SpringSource.class.getClassLoader(); cl != null; cl = cl.getParent()) {
            list.add(cl);
        }

        URI uri = URI.create("jar:" + Paths.get("target/test.zip").toUri());
        Map<String, ?> env = Collections.singletonMap("create", "true");
        try (FileSystem fs = FileSystems.newFileSystem(uri, env)) {
            Path target = fs.getPath("/");

            list.stream()
                    .filter(URLClassLoader.class::isInstance)
                    .map(URLClassLoader.class::cast)
                    .map(URLClassLoader::getURLs)
                    .flatMap(Arrays::stream)
                    .forEach(url -> resolve(url, target));
        }

    }

    private static void resolve(URL url, Path target) {
        try {
            resolve0(url, target);
        } catch (URISyntaxException | IOException ex) {
            throw new Error(ex);
        }
    }

    private static void resolve0(URL url, Path target) throws URISyntaxException, IOException {
        Path path = Paths.get(url.toURI());

        if (Files.isDirectory(path)) {
            return;
        }
        String fileName = path.getFileName().toString();
        if (!fileName.endsWith(".jar")) {
            throw new IllegalStateException();
        }

        if (!fileName.contains("spring-")) {
            return;
        }

        Path resolve = path.getParent().resolve(fileName.substring(0, fileName.length() - 4) + "-sources.jar");
        if (!Files.isReadable(path)) {
            throw new IllegalArgumentException(resolve.toString());
        }
        try (FileSystem fs = FileSystems.newFileSystem(URI.create("jar:" + resolve.toUri()), Collections.emptyMap())) {
            Path source = fs.getPath("/");

            DirectoryCopyHelper.copy(source, target,
                    p -> Files.isDirectory(p) || p.getFileName().toString().endsWith(".java"));
        }

    }

}
