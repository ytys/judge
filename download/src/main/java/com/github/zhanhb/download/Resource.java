/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
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

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

public abstract class Resource {

    // -------------------------------------------------------------- Constants
    /**
     * HTTP date format.
     */
    private static final SimpleDateFormat FORMATTER;

    /**
     * GMT timezone - all HTTP dates are on GMT
     */
    static {
        FORMATTER = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.US);
        FORMATTER.setTimeZone(TimeZone.getTimeZone("GMT"));
    }
    private String lastModifiedHttp;
    private String weakETag;

    abstract boolean exists() throws IOException;

    abstract String getName();

    /**
     * Get content length.
     *
     * @return content length value
     * @throws java.io.IOException when an I/O exception occur.
     */
    abstract long getContentLength() throws IOException;

    /**
     * Get last modified time.
     *
     * @return lastModified time value
     * @throws java.io.IOException
     */
    abstract long getLastModified() throws IOException;

    /**
     * @return Returns the lastModifiedHttp.
     */
    String getLastModifiedHttp() {
        if (lastModifiedHttp != null) {
            return lastModifiedHttp;
        }
        try {
            long modifiedDate = getLastModified();
            if (modifiedDate >= 0) {
                synchronized (FORMATTER) {
                    lastModifiedHttp = FORMATTER.format(modifiedDate);
                }
            }
        } catch (IOException ex) {
        }
        return lastModifiedHttp;
    }

    public abstract String getMimeType();

    /**
     * Get ETag.
     *
     * @return strong ETag if available, else weak ETag.
     */
    String getETag() {
        String result = weakETag;
        // The weakETag is contentLength + lastModified
        if (result == null) {
            try {
                long contentLength = getContentLength();
                long lastModified = getLastModified();
                if ((contentLength >= 0) || (lastModified >= 0)) {
                    result = "W/\"" + contentLength + "-"
                            + lastModified + "\"";
                }
                weakETag = result;
            } catch (IOException ex) {
            }
        }
        return result;
    }

    /**
     * Always open a new input stream.
     *
     * @return a new input stream.
     * @throws IOException
     */
    abstract InputStream openStream() throws IOException;

}
