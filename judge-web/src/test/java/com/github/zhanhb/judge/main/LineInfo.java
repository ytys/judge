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

/**
 *
 * @author zhanhb
 * @date Jun 2, 2015, 1:15:39
 */
public class LineInfo {

    private final int lineStart;
    private final int columnStart;
    private final int lineEnd;
    private final int columnEnd;

    public LineInfo(String content, int start, int end) {
        this.lineStart = content.substring(0, start).replaceAll("[^\n]", "").length() + 1;
        this.columnStart = start - content.lastIndexOf('\n', start);
        this.lineEnd = content.substring(0, end).replaceAll("[^\n]", "").length() + 1;
        this.columnEnd = end - content.lastIndexOf('\n', end - 1);
    }

    public int getLineStart() {
        return lineStart;
    }

    public int getColumnStart() {
        return columnStart;
    }

    public int getLineEnd() {
        return lineEnd;
    }

    public int getColumnEnd() {
        return columnEnd;
    }

    @Override
    public String toString() {
        if (lineStart == lineEnd) {
            return "(line " + lineStart + ", column " + columnStart + " to " + columnEnd + ")";
        }
        return "(line " + lineStart + ", column " + columnStart + " to line " + lineEnd + ", column " + columnEnd + ")";
    }
}
