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
package com.github.zhanhb.download;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

/**
 *
 * @author zhanhb
 * @date Jun 6, 2015, 0:36:42
 */
public class Resources {

    public static Builder of(File file) {
        return new Builder(file);
    }

    /**
     * Build resource with an input stream. The inputStream will be closed
     * automatically.
     *
     * @param inputStream
     * @return
     */
    public static Builder of(InputStream inputStream) {
        return new Builder(inputStream);
    }

    private Resources() {
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    @SuppressWarnings("PublicInnerClass")
    public static class Builder {

        private File file;
        private InputStream inputStream;
        private String name;
        private long lastModified = -1;
        private String mimeType;
        private long contentLength = -1;

        private Builder(File file) {
            if (file == null) {
                throw new NullPointerException("file");
            }
            this.file = file;
        }

        private Builder(InputStream inputStream) {
            if (inputStream == null) {
                throw new NullPointerException("inputStream");
            }
            this.inputStream = inputStream;
        }

        public String name() {
            return name;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public long lastModified() {
            if (file != null) {
                return file.lastModified();
            }
            return lastModified;
        }

        public Builder lastModified(long lastModified) {
            if (file != null) {
                throw new IllegalStateException("file last modified should not be changed in the builder");
            }
            this.lastModified = lastModified;
            return this;
        }

        public Builder lastModified(Date date) {
            if (file != null) {
                throw new IllegalStateException("file last modified should not be changed in the builder");
            }
            this.lastModified = date != null ? date.getTime() : -1;
            return this;
        }

        public long contentLength() {
            if (file != null) {
                return file.length();
            }
            return contentLength;
        }

        public Builder contentLength(long contentLength) {
            if (file != null) {
                throw new IllegalStateException("file content length should not be changed in the builder");
            }
            this.contentLength = contentLength;
            return this;
        }

        public String mimeType() {
            return mimeType;
        }

        public Builder mimeType(String mimeType) {
            this.mimeType = mimeType;
            return this;
        }

        public Resource build() {
            if (file != null) {
                return new FileResource(file, name, mimeType);
            }
            assert inputStream != null;
            return new InputStreamResource(inputStream, name, lastModified, contentLength, mimeType);
        }
    }

    private static class FileResource extends Resource {

        private final File file;
        private final String name;
        private final String mimeType;

        private FileResource(File file, String name, String mimeType) {
            this.file = file;
            this.name = name;
            this.mimeType = mimeType;
        }

        @Override
        public boolean exists() {
            return file.exists();
        }

        @Override
        public String getName() {
            return name != null ? name : file.getName();
        }

        @Override
        public InputStream streamContent() throws IOException {
            return new FileInputStream(file);
        }

        @Override
        public long getContentLength() {
            return file.length();
        }

        @Override
        public long getLastModified() {
            return file.lastModified();
        }

        @Override
        public String getMimeType() {
            return mimeType;
        }
    }

    private static class InputStreamResource extends Resource {

        private final InputStream stream;
        private final String name;
        private final long lastModified;
        private final long contentLength;
        private final String mimeType;

        private InputStreamResource(InputStream stream, String name, long lastModified, long contentLength, String mimeType) {
            this.stream = stream;
            this.name = name;
            this.lastModified = lastModified;
            this.contentLength = contentLength;
            this.mimeType = mimeType;
        }

        @Override
        public boolean exists() {
            return stream != null;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public InputStream streamContent() throws IOException {
            return stream;
        }

        @Override
        public long getContentLength() {
            return contentLength;
        }

        @Override
        public long getLastModified() {
            return lastModified;
        }

        @Override
        public String getMimeType() {
            return mimeType;
        }
    }
}
