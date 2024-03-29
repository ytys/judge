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
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 *
 * @author zhanhb
 */
@Slf4j
public class DirectoryCopyHelperTest {

    /**
     * Test of copy method, of class DirectoryCopyHelper.
     *
     * @throws java.io.IOException
     */
    @Test
    public void testCopy() throws IOException {
        log.info("copy");
        Path zipfile = Files.createTempFile("", ".zip");
        Files.delete(zipfile);
        try (FileSystem fs = FileSystems.newFileSystem(URI.create("jar:" + zipfile.toUri()), Collections.singletonMap("create", "true"))) {
            DirectoryCopyHelper.copy(Paths.get("src"), fs.getPath("/"));
        } finally {
            Files.delete(zipfile);
        }
    }

    @Test
    public void testConstructor() throws Exception {
        TestUtils.testConstructor(DirectoryCopyHelper.class);
    }

}
