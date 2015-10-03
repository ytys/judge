/*
 * Copyright 2014 zhanhb.
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

import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;

/**
 *
 * @author zhanhb
 */
public class DirectoryCopyHelper {

    public static Path copy(Path source, Path target) throws IOException {
        return copy(source, target, (PathMatcher) null);
    }

    public static Path copy(Path source, Path target, CopyOption... options) throws IOException {
        return copy(source, target, (PathMatcher) null, options);
    }

    public static Path copy(Path source, Path target, CopyOption[] fileOptions, final CopyOption[] dirOptions) throws IOException {
        return copy(source, target, null, fileOptions, dirOptions);
    }

    public static Path copy(Path source, Path target, PathMatcher pathMatcher) throws IOException {
        return copy(source, target, pathMatcher, StandardCopyOption.COPY_ATTRIBUTES, StandardCopyOption.REPLACE_EXISTING);
    }

    public static Path copy(Path source, Path target, PathMatcher pathMatcher, CopyOption... options) throws IOException {
        return copy(source, target, pathMatcher, options, options);
    }

    public static Path copy(Path source, Path target, PathMatcher pathMatcher, CopyOption[] fileOptions, final CopyOption[] dirOptions) throws IOException {
        return Files.walkFileTree(source, new SimpleFileVisitor<Path>() {

            private final PathMatcher matcher = pathMatcher == null ? __ -> true : pathMatcher;
            private final CopyOptions dirOptions2 = CopyOptions.parse(dirOptions);

            protected Path resolve(Path dir) {
                return target.resolve(source.relativize(dir).toString());
            }

            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                if (!matcher.matches(dir)) {
                    return FileVisitResult.SKIP_SUBTREE;
                }
                Path resolve = resolve(dir);
                if (resolve.getNameCount() > 0) {
                    boolean exists = Files.exists(resolve);
                    if (!dirOptions2.replaceExisting && exists) {
                        throw new FileAlreadyExistsException(resolve.toString());
                    }

                    // create directory
                    if (exists) {
                        if (dirOptions2.copyAttributes) { // copy basic attributes to target
                            copyAttribute(attrs, resolve);
                        }
                    } else {
                        Files.copy(dir, resolve, dirOptions);
                    }
                }
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                if (matcher.matches(file)) {
                    Files.copy(file, resolve(file), fileOptions);
                }
                return FileVisitResult.CONTINUE;
            }

            @SuppressWarnings({"BroadCatchBlock", "TooBroadCatch"})
            private void copyAttribute(BasicFileAttributes attrs, Path path) throws IOException {
                BasicFileAttributeView view = Files.getFileAttributeView(path, BasicFileAttributeView.class);
                try {
                    view.setTimes(attrs.lastModifiedTime(),
                            attrs.lastAccessTime(),
                            attrs.creationTime());
                } catch (Throwable x) {
                    // rollback
                    try {
                        Files.delete(path);
                    } catch (Throwable suppressed) {
                        x.addSuppressed(suppressed);
                    }
                    throw x;
                }
            }

        });
    }

    private DirectoryCopyHelper() {
        throw new AssertionError();
    }

    private static class CopyOptions {

        private static CopyOptions parse(CopyOption[] options) {
            CopyOptions result = new CopyOptions();
            for (CopyOption option : options) {
                if (option == StandardCopyOption.REPLACE_EXISTING) {
                    result.replaceExisting = true;
                    continue;
                }
                if (option == StandardCopyOption.COPY_ATTRIBUTES) {
                    result.copyAttributes = true;
                    continue;
                }
                if (option == null) {
                    throw new NullPointerException();
                }
                // ignore
            }
            return result;
        }

        boolean replaceExisting = false;
        boolean copyAttributes = false;

    }

}
