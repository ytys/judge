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
package com.github.zhanhb.judge;

import com.github.zhanhb.judge.main.LineInfo;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import static org.junit.Assert.fail;
import org.junit.AssumptionViolatedException;
import org.junit.Test;

/**
 * we don't have jspx now.
 *
 * @author zhanhb
 * @date Jun 1, 2015, 14:15:38
 */
public class JSPXCloseTagTest {

    private Stream<Path> getStream() throws IOException {
        return Files.walk(Paths.get("src"))
                .filter(Files::isRegularFile)
                .filter(path -> path.toString().matches("(?i).+\\.(?:jsp|tag)x$"));
    }

    @Test
    public void checkJspxTagx() throws IOException {
        List<Path> paths = getStream().collect(Collectors.toList());
        if (0 == paths.size()) {
            throw new AssumptionViolatedException("no jspx, tagx files found");
        }
        for (Path path : paths) {
            check(path);
        }
    }

    private void check(Path path) throws IOException {
        final String idStart = "[a-zA-Z_\u007F-\uFFFF]";
        // the identity part has colon in it, but colon is ok for us.
        // sample:
        // <html:script src="..."></html:script>
        // the content of this tag is empty, but ok.
        final String idPart = "[a-zA-Z0-9_\\.\u007F-\uFFFF-]";
        final String tagNamePattern = idStart + idPart + "*+";
        final String patternStr = "<(?<tagName>" + tagNamePattern + ")[^>]*+>[\r\n\t\\s]*+</\\k<tagName>(?:\\s++[^>]*+)?\\>";
        String str = Files.readAllLines(path, StandardCharsets.UTF_8)
                .stream()
                .collect(Collectors.joining("\n"));
        Matcher matcher = Pattern.compile(patternStr).matcher(str);
        if (matcher.find()) {
            String tagName = matcher.group("tagName");

            LineInfo lineInfo = new LineInfo(str, matcher.start(), matcher.end());

            String extension = path.toString();
            extension = extension.substring(extension.lastIndexOf('.') + 1).toLowerCase();
            String message = "Find empty '" + tagName + "' tag in file '" + path + "' " + lineInfo
                    + ", but " + extension + " file should not hava empty content tags, for it's output will be confused.";
            fail(message);
        }
    }
}
