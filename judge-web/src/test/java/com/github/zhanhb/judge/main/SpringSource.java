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

    private static Path target;

    static {
        URI uri = URI.create("jar:" + Paths.get("target/test.zip").toUri());
        Map<String, ?> env = Collections.singletonMap("create", "true");
        try {
            target = FileSystems.newFileSystem(uri, env).getRootDirectories().iterator().next();
        } catch (IOException ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static void main(String[] args) throws IOException {
        ArrayList<ClassLoader> list = new ArrayList<>(20);
        for (ClassLoader cl = SpringSource.class.getClassLoader(); cl != null; cl = cl.getParent()) {
            list.add(cl);
        }

        list.stream()
                .filter(cl -> cl instanceof URLClassLoader)
                .map(URLClassLoader.class::cast)
                .flatMap(cl -> Arrays.stream(cl.getURLs()))
                .forEach((URL url) -> {
                    try {
                        resolve(url);
                    } catch (URISyntaxException | IOException ex) {
                        throw new Error(ex);
                    }
                });
        target.getFileSystem().close();

    }

    private static void resolve(URL url) throws URISyntaxException, IOException {
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
        FileSystem fs = FileSystems.newFileSystem(new URI("jar:" + resolve.toUri()), Collections.emptyMap());

        Path source = fs.getRootDirectories().iterator().next();

        DirectoryCopyHelper.copy(source, target,
                p -> Files.isDirectory(p) || p.getFileName().toString().endsWith(".java"));

    }

}
