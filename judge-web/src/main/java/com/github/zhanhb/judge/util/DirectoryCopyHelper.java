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
import java.io.InputStream;
import java.nio.file.CopyOption;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.FileVisitResult;
import static java.nio.file.FileVisitResult.CONTINUE;
import static java.nio.file.FileVisitResult.SKIP_SUBTREE;
import java.nio.file.Files;
import static java.nio.file.Files.getFileAttributeView;
import static java.nio.file.Files.walkFileTree;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.SimpleFileVisitor;
import static java.nio.file.StandardCopyOption.COPY_ATTRIBUTES;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author zhanhb
 */
@Slf4j
public class DirectoryCopyHelper extends SimpleFileVisitor<Path> {

    public static Path copy(Path source, Path target) throws IOException {
        return walkFileTree(source, new DirectoryCopyHelper(source, target));
    }

    public static Path copy(Path source, Path target, CopyOption... options) throws IOException {
        return walkFileTree(source, new DirectoryCopyHelper(source, target, options));
    }

    public static Path copy(Path source, Path target, CopyOption[] fileOptions, final CopyOption[] dirOptions) throws IOException {
        return walkFileTree(source, new DirectoryCopyHelper(source, target, fileOptions, dirOptions));
    }

    public static Path copy(Path source, Path target, PathMatcher pathMatcher) throws IOException {
        return walkFileTree(source, new DirectoryCopyHelper(source, target, pathMatcher));
    }

    public static Path copy(Path source, Path target, PathMatcher pathMatcher, CopyOption... options) throws IOException {
        return walkFileTree(source, new DirectoryCopyHelper(source, target, pathMatcher, options));
    }

    public static Path copy(Path source, Path target, PathMatcher pathMatcher, CopyOption[] fileOptions, final CopyOption[] dirOptions) throws IOException {
        return walkFileTree(source, new DirectoryCopyHelper(source, target, pathMatcher, fileOptions, dirOptions));
    }

    private final Path source;
    private final Path target;
    private final CopyOption[] fileOptions;
    private final CopyOptions dirOptions;
    private final PathMatcher pathMatcher;

    protected DirectoryCopyHelper(Path source, Path target) {
        this(source, target, (PathMatcher) null);
    }

    protected DirectoryCopyHelper(Path source, Path target, CopyOption[] options) {
        this(source, target, (PathMatcher) null, options);
    }

    protected DirectoryCopyHelper(Path source, Path target, CopyOption[] fileOptions, CopyOption[] dirOptions) {
        this(source, target, null, fileOptions, dirOptions);
    }

    protected DirectoryCopyHelper(Path source, Path target, PathMatcher pathMatcher) {
        this(source, target, pathMatcher, COPY_ATTRIBUTES, REPLACE_EXISTING);
    }

    protected DirectoryCopyHelper(Path source, Path target, PathMatcher pathMatcher, CopyOption... options) {
        this(source, target, pathMatcher, options, options);
    }

    protected DirectoryCopyHelper(Path source, Path target, PathMatcher pathMatcher, CopyOption[] fileOptions, CopyOption[] dirOptions) {
        this.source = Objects.requireNonNull(source, "source");
        this.target = Objects.requireNonNull(target, "target");
        this.pathMatcher = pathMatcher == null ? __ -> true : pathMatcher;
        this.dirOptions = CopyOptions.parse(Objects.requireNonNull(dirOptions, "dir options"));
        this.fileOptions = Objects.requireNonNull(fileOptions, "file options");
    }

    protected Path resolve(Path dir) {
        return target.resolve(source.relativize(dir).toString());
    }

    @Override
    @SuppressWarnings({"BroadCatchBlock", "TooBroadCatch"})
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
        if (pathMatcher.matches(dir)) {

            if (attrs.isSymbolicLink()) {
                throw new IOException("Copying of symbolic links not supported");
            }

            Path resolve = resolve(dir);
            boolean exists = Files.exists(resolve);
            if (resolve.getNameCount() > 0 && !dirOptions.replaceExisting && exists) {
                throw new FileAlreadyExistsException(resolve.toString());
            }

            // create directory or copy file
            if (attrs.isDirectory()) {
                if (!exists) {
                    Files.createDirectory(resolve);
                }
            } else {
                try (InputStream in = Files.newInputStream(dir)) {
                    Files.copy(in, resolve);
                }
            }

            // copy basic attributes to target
            if (resolve.getNameCount() > 0 && dirOptions.copyAttributes) {
                BasicFileAttributeView view
                        = getFileAttributeView(resolve, BasicFileAttributeView.class);
                try {
                    view.setTimes(attrs.lastModifiedTime(),
                            attrs.lastAccessTime(),
                            attrs.creationTime());
                } catch (Throwable x) {
                    // rollback
                    try {
                        Files.delete(resolve);
                    } catch (Throwable suppressed) {
                        x.addSuppressed(suppressed);
                    }
                    throw x;
                }
            }
            return CONTINUE;
        }
        return SKIP_SUBTREE;
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
        if (pathMatcher.matches(file)) {
            Files.copy(file, resolve(file), fileOptions);
            return CONTINUE;
        }
        return SKIP_SUBTREE;
    }

    private static class CopyOptions {

        private static CopyOptions parse(CopyOption[] options) {
            CopyOptions result = new CopyOptions();
            for (CopyOption option : options) {
                if (option == REPLACE_EXISTING) {
                    result.replaceExisting = true;
                    continue;
                }
                if (option == COPY_ATTRIBUTES) {
                    result.copyAttributes = true;
                    continue;
                }
                if (option == null) {
                    throw new NullPointerException();
                }
                throw new UnsupportedOperationException("'" + option
                        + "' is not a recognized copy option");
            }
            return result;
        }

        boolean replaceExisting = false;
        boolean copyAttributes = false;

    }

}
