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
package com.github.zhanhb.judge.util;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;

/**
 *
 * @author zhanhb
 */
@Utility
public class Zip {

    public void zip(Path source, Path target) throws IOException {
        try (OutputStream os = Files.newOutputStream(target)) {
            zip(source, os);
        }
    }

    public void zip(Path source, OutputStream target) throws IOException {
        try (ZipArchiveOutputStream zip = new ZipArchiveOutputStream(target)) {
            Files.walk(source)
                    .filter(file -> Files.isRegularFile(file))
                    .forEach((Path file) -> copy(file, source, zip));
        } catch (UncheckedIOException ex) {
            throw ex.getCause();
        }
    }

    protected void copy(Path file, Path source, ZipArchiveOutputStream zip) {
        try {
            BasicFileAttributes attrs = Files.readAttributes(file, BasicFileAttributes.class);

            String name = source.relativize(file).toString();

            ZipArchiveEntry entry = new ZipArchiveEntry(name);
            entry.setCreationTime(attrs.creationTime());
            entry.setLastAccessTime(attrs.lastAccessTime());
            entry.setLastModifiedTime(attrs.lastModifiedTime());
            entry.setSize(attrs.size());

            zip.putArchiveEntry(entry);
            Files.copy(file, zip);
            zip.closeArchiveEntry();
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }

}
