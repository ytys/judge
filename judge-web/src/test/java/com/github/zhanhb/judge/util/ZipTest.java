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

import com.github.zhanhb.judge.main.Finder;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.apache.commons.compress.archivers.ArchiveException;
import org.junit.Test;

/**
 *
 * @author zhanhb
 */
public class ZipTest {

    @Test
    public void test() throws IOException, ArchiveException {
        Zip zip = new Zip();
        Path zipfile = Files.createTempFile("", ".zip");
        try {
            zip.zip(zipfile, Paths.get("src"));
            Finder.consume(ZipTest.class, url -> {
                try {
                    zip.zip(zipfile, Paths.get(url.toURI()));
                } catch (URISyntaxException | IOException ex) {
                    throw new RuntimeException(ex);
                }
            });
        } finally {
            Files.delete(zipfile);
        }
    }

}
