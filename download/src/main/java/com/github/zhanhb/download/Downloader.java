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

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Downloader {

    private static final Logger LOG = LoggerFactory.getLogger(Downloader.class);

    /**
     * Full range marker.
     */
    private static final List<Range> FULL = Collections.emptyList();

    // ----------------------------------------------------- Static Initializer
    /**
     * MIME multipart separation string
     */
    private static final String MIME_SEPARATION = "DOWNLOADER_MIME_BOUNDARY";

    public static Downloader newInstance() {
        return new Downloader();
    }

    // ----------------------------------------------------- Instance Variables
    /**
     * The input buffer size to use when serving resources.
     */
    private int input = 4096;

    /**
     * The output buffer size to use when serving resources.
     */
    private int outputBufferSize = 4096;

    /**
     * Should the Accept-Ranges: bytes header be send with static resources?
     */
    private boolean useAcceptRanges = true;

    /**
     * Should the Content-Disposition: attachment; filename=... header be sent
     * with static resources?
     */
    private ContentDisposition contentDisposition = SimpleContentDisposition.ATTACHMENT;

    private Downloader() {
    }

    // ------------------------------------------------------ public Methods
    public Downloader useAcceptRanges(boolean useAcceptRanges) {
        this.useAcceptRanges = useAcceptRanges;
        return this;
    }

    public Downloader outputBufferSize(int output) {
        this.outputBufferSize = output;
        return this;
    }

    public Downloader inputBufferSize(int input) {
        this.input = input;
        return this;
    }

    public int inputBufferSize() {
        return input;
    }

    public int outputBufferSize() {
        return outputBufferSize;
    }

    public Downloader contentDisposition(ContentDisposition contentDisposition) {
        this.contentDisposition = contentDisposition;
        return this;
    }

    /**
     * Process a HEAD request for the specified resource.
     *
     * @param request The servlet request we are processing
     * @param response The servlet response we are creating
     * @param resource
     *
     * @exception IOException if an input/output error occurs
     */
    public void doHead(HttpServletRequest request, HttpServletResponse response,
            Resource resource) throws IOException {
        // Serve the requested resource, without the data content
        serveResource(request, response, false, resource);
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response,
            Resource resource) throws IOException {
        serveResource(request, response, true, resource);
    }

    /**
     * Check if the conditions specified in the optional If headers are
     * satisfied.
     *
     * @param request The servlet request we are processing
     * @param response The servlet response we are creating
     * @param resource The resource information
     * @return boolean true if the resource meets all the specified conditions,
     * and false if any of the conditions is not satisfied, in which case
     * request processing is stopped
     */
    private boolean checkIfHeaders(HttpServletRequest request,
            HttpServletResponse response,
            Resource resource)
            throws IOException {
        return checkIfMatch(request, response, resource)
                && checkIfModifiedSince(request, response, resource)
                && checkIfNoneMatch(request, response, resource)
                && checkIfUnmodifiedSince(request, response, resource);
    }

    /**
     * Serve the specified resource, optionally including the data content.
     *
     * @param request The servlet request we are processing
     * @param response The servlet response we are creating
     * @param content Should the content be included?
     * @param resource the resource to serve
     *
     * @exception IOException if an input/output error occurs
     * @exception ServletException if a servlet-specified error occurs
     */
    private void serveResource(HttpServletRequest request, HttpServletResponse response,
            boolean content, Resource resource)
            throws IOException {
        boolean serveContent = content;
        if (resource == null || !resource.exists()) {
            // Check if we're included so we can return the appropriate
            // missing resource name in the error
            String requestUri = (String) request.getAttribute(RequestDispatcher.INCLUDE_REQUEST_URI);
            if (requestUri == null) {
                requestUri = request.getRequestURI();
            } else {
                // We're included
                // SRV.9.3 says we must throw a FNFE
                throw new FileNotFoundException("The requested resource (" + requestUri + ") is not available");
            }
            response.sendError(HttpServletResponse.SC_NOT_FOUND, requestUri);
            return;
        }
        boolean isError = response.getStatus() >= HttpServletResponse.SC_BAD_REQUEST;
        // Check if the conditions specified in the optional If headers are
        // satisfied.
        // Checking If headers
        boolean included = (request.getAttribute(RequestDispatcher.INCLUDE_CONTEXT_PATH) != null);
        if (!included && !isError && !checkIfHeaders(request, response, resource)) {
            return;
        }
        // Find content type.
        String contentType = resource.getMimeType();
        if (contentType == null) {
            contentType = request.getServletContext().getMimeType(resource.getName());
        }
        List<Range> ranges = null;
        long contentLength;
        if (!isError) {
            if (useAcceptRanges) {
                // Accept ranges header
                response.setHeader("Accept-Ranges", "bytes");
            }
            // Parse range specifier
            ranges = parseRange(request, response, resource);
            // ETag header
            response.setHeader("ETag", resource.getETag());
            // Last-Modified header
            response.setHeader("Last-Modified", resource.getLastModifiedHttp());
        }
        // Get content length
        contentLength = resource.getContentLength();
        // Special case for zero length files, which would cause a
        // (silent) ISE when setting the output buffer size
        if (contentLength == 0L) {
            serveContent = false;
        }
        ServletOutputStream ostream = null;
        if (serveContent) {
            // Trying to retrieve the servlet output stream
            try {
                ostream = response.getOutputStream();
            } catch (IllegalStateException e) {
                // If it fails, we try to get a Writer instead if we're
                // trying to serve a text file
                throw e;
            }
        }

        ContentDisposition cd = this.contentDisposition;
        if (cd != null) {
            cd.setContentDisposition(request, response, resource.getName());
        }

        // Check to see if a Filter, Valve of wrapper has written some content.
        // If it has, disable range requests and setting of a content length
        // since neither can be done reliably.
        if (isError || ((ranges == null || ranges.isEmpty()) && request.getHeader("Range") == null)
                || ranges == FULL) {
            // Set the appropriate output headers
            if (contentType != null) {
                LOG.debug("serveFile:  contentType='{}'", contentType);
                response.setContentType(contentType);
            }
            if ((contentLength >= 0) && (!serveContent || ostream != null)) {
                LOG.debug("serveFile:  contentLength={}", contentLength);
                // Don't set a content length if something else has already
                // written to the response.
                if (contentLength < Integer.MAX_VALUE) {
                    response.setContentLength((int) contentLength);
                } else {
                    // Set the content-length as String to be able to use a long
                    response.setHeader("content-length", "" + contentLength);
                }
            }
            // Copy the input stream to our output stream (if requested)
            if (serveContent) {
                try {
                    response.setBufferSize(outputBufferSize);
                } catch (IllegalStateException e) {
                    // Silent catch
                }
                if (ostream != null) {
                    copy(resource, ostream);
                } else {
                    throw new IllegalStateException();
                }
            }
        } else {
            if ((ranges == null) || (ranges.isEmpty())) {
                return;
            }
            // Partial content response.
            response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
            if (ranges.size() == 1) {
                Range range = ranges.get(0);
                response.addHeader("Content-Range", "bytes " + range.start + "-" + range.end + "/" + range.length);
                long length = range.end - range.start + 1;
                if (length < Integer.MAX_VALUE) {
                    response.setContentLength((int) length);
                } else {
                    // Set the content-length as String to be able to use a long
                    response.setHeader("content-length", "" + length);
                }
                if (contentType != null) {
                    LOG.debug("serveFile:  contentType='{}'", contentType);
                    response.setContentType(contentType);
                }
                if (serveContent) {
                    try {
                        response.setBufferSize(outputBufferSize);
                    } catch (IllegalStateException e) {
                        // Silent catch
                    }
                    if (ostream != null) {
                        copy(resource, ostream, range);
                    } else {
                        // we should not get here
                        throw new IllegalStateException();
                    }
                }
            } else {
                response.setContentType("multipart/byteranges; boundary=" + MIME_SEPARATION);
                if (serveContent) {
                    try {
                        response.setBufferSize(outputBufferSize);
                    } catch (IllegalStateException e) {
                        // Silent catch
                    }
                    if (ostream != null) {
                        copy(resource, ostream, ranges.iterator(),
                                contentType);
                    } else {
                        // we should not get here
                        throw new IllegalStateException();
                    }
                }
            }
        }
    }

    /**
     * Parse the range header.
     *
     * @param request The servlet request we are processing
     * @param response The servlet response we are creating
     * @param resource
     * @return Vector of ranges
     */
    @SuppressWarnings("ReturnOfCollectionOrArrayField")
    private List<Range> parseRange(HttpServletRequest request,
            HttpServletResponse response,
            Resource resource) throws IOException {
        // Checking If-Range
        String headerValue = request.getHeader("If-Range");
        if (headerValue != null) {
            long headerValueTime = (-1);
            try {
                headerValueTime = request.getDateHeader("If-Range");
            } catch (IllegalArgumentException e) {
                // Ignore
            }
            String eTag = resource.getETag();
            long lastModified = resource.getLastModified();
            if (headerValueTime == (-1)) {
                // If the ETag the client gave does not match the entity
                // etag, then the entire entity is returned.
                if (!eTag.equals(headerValue.trim())) {
                    return FULL;
                }
            } else // If the timestamp of the entity the client got is older than
            // the last modification date of the entity, the entire entity
            // is returned.
            {
                if (lastModified > (headerValueTime + 1000)) {
                    return FULL;
                }
            }
        }
        long fileLength = resource.getContentLength();
        if (fileLength == 0) {
            return null;
        }
        // Retrieving the range header (if any is specified
        String rangeHeader = request.getHeader("Range");
        if (rangeHeader == null) {
            return null;
        }
        // bytes is the only range unit supported (and I don't see the point
        // of adding new ones).
        if (!rangeHeader.startsWith("bytes")) {
            response.addHeader("Content-Range", "bytes */" + fileLength);
            response.sendError(HttpServletResponse.SC_REQUESTED_RANGE_NOT_SATISFIABLE);
            return null;
        }
        rangeHeader = rangeHeader.substring(6);
        // Vector which will contain all the ranges which are successfully
        // parsed.
        StringTokenizer commaTokenizer = new StringTokenizer(rangeHeader, ",");
        ArrayList<Range> result = new ArrayList<>(commaTokenizer.countTokens());
        // Parsing the range list
        while (commaTokenizer.hasMoreTokens()) {
            String rangeDefinition = commaTokenizer.nextToken().trim();
            Range currentRange = new Range();
            currentRange.length = fileLength;
            int dashPos = rangeDefinition.indexOf('-');
            if (dashPos == -1) {
                response.addHeader("Content-Range", "bytes */" + fileLength);
                response.sendError(HttpServletResponse.SC_REQUESTED_RANGE_NOT_SATISFIABLE);
                return null;
            }
            if (dashPos == 0) {
                try {
                    long offset = Long.parseLong(rangeDefinition);
                    currentRange.start = fileLength + offset;
                    currentRange.end = fileLength - 1;
                } catch (NumberFormatException e) {
                    response.addHeader("Content-Range", "bytes */" + fileLength);
                    response.sendError(HttpServletResponse.SC_REQUESTED_RANGE_NOT_SATISFIABLE);
                    return null;
                }
            } else {
                try {
                    currentRange.start = Long.parseLong(rangeDefinition.substring(0, dashPos));
                    if (dashPos < rangeDefinition.length() - 1) {
                        currentRange.end = Long.parseLong(rangeDefinition.substring(dashPos + 1, rangeDefinition.length()));
                    } else {
                        currentRange.end = fileLength - 1;
                    }
                } catch (NumberFormatException e) {
                    response.addHeader("Content-Range", "bytes */" + fileLength);
                    response.sendError(HttpServletResponse.SC_REQUESTED_RANGE_NOT_SATISFIABLE);
                    return null;
                }
            }
            if (!currentRange.validate()) {
                response.addHeader("Content-Range", "bytes */" + fileLength);
                response.sendError(HttpServletResponse.SC_REQUESTED_RANGE_NOT_SATISFIABLE);
                return null;
            }
            result.add(currentRange);
        }
        return result;
    }

    /**
     * Check if the if-match condition is satisfied.
     *
     * @param request The servlet request we are processing
     * @param response The servlet response we are creating
     * @param resource File object
     * @return boolean true if the resource meets the specified condition, and
     * false if the condition is not satisfied, in which case request processing
     * is stopped
     */
    private boolean checkIfMatch(HttpServletRequest request,
            HttpServletResponse response,
            Resource resource)
            throws IOException {
        String eTag = resource.getETag();
        String headerValue = request.getHeader("If-Match");
        if (headerValue != null) {
            if (headerValue.indexOf('*') == -1) {
                StringTokenizer commaTokenizer = new StringTokenizer(headerValue, ",");
                boolean conditionSatisfied = false;
                while (commaTokenizer.hasMoreTokens()) {
                    String currentToken = commaTokenizer.nextToken();
                    if (currentToken.trim().equals(eTag)) {
                        conditionSatisfied = true;
                        break;
                    }
                }
                // If none of the given ETags match, 412 Precodition failed is
                // sent back
                if (!conditionSatisfied) {
                    response.sendError(HttpServletResponse.SC_PRECONDITION_FAILED);
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Check if the if-modified-since condition is satisfied.
     *
     * @param request The servlet request we are processing
     * @param response The servlet response we are creating
     * @param resource File object
     * @return boolean true if the resource meets the specified condition, and
     * false if the condition is not satisfied, in which case request processing
     * is stopped
     */
    private boolean checkIfModifiedSince(HttpServletRequest request,
            HttpServletResponse response,
            Resource resource) {
        try {
            long headerValue = request.getDateHeader("If-Modified-Since");
            long lastModified = resource.getLastModified();
            if (headerValue != -1) {
                // If an If-None-Match header has been specified, if modified since
                // is ignored.
                if ((request.getHeader("If-None-Match") == null)
                        && (lastModified < headerValue + 1000)) {
                    // The entity has not been modified since the date
                    // specified by the client. This is not an error case.
                    response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
                    response.setHeader("ETag", resource.getETag());
                    return false;
                }
            }
        } catch (IllegalArgumentException | IOException ex) {
            return true;
        }
        return true;
    }

    /**
     * Check if the if-none-match condition is satisfied.
     *
     * @param request The servlet request we are processing
     * @param response The servlet response we are creating
     * @param resource File object
     * @return boolean true if the resource meets the specified condition, and
     * false if the condition is not satisfied, in which case request processing
     * is stopped
     */
    private boolean checkIfNoneMatch(HttpServletRequest request,
            HttpServletResponse response,
            Resource resource)
            throws IOException {
        String eTag = resource.getETag();
        String headerValue = request.getHeader("If-None-Match");
        if (headerValue != null) {
            boolean conditionSatisfied = false;
            if (!headerValue.equals("*")) {
                StringTokenizer commaTokenizer = new StringTokenizer(headerValue, ",");
                while (!conditionSatisfied && commaTokenizer.hasMoreTokens()) {
                    String currentToken = commaTokenizer.nextToken();
                    if (currentToken.trim().equals(eTag)) {
                        conditionSatisfied = true;
                    }
                }
            } else {
                conditionSatisfied = true;
            }
            if (conditionSatisfied) {
                // For GET and HEAD, we should respond with
                // 304 Not Modified.
                // For every other method, 412 Precondition Failed is sent
                // back.
                if (("GET".equals(request.getMethod())) || ("HEAD".equals(request.getMethod()))) {
                    response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
                    response.setHeader("ETag", eTag);
                    return false;
                }
                response.sendError(HttpServletResponse.SC_PRECONDITION_FAILED);
                return false;
            }
        }
        return true;
    }

    /**
     * Check if the if-unmodified-since condition is satisfied.
     *
     * @param request The servlet request we are processing
     * @param response The servlet response we are creating
     * @param resource File object
     * @return boolean true if the resource meets the specified condition, and
     * false if the condition is not satisfied, in which case request processing
     * is stopped
     */
    private boolean checkIfUnmodifiedSince(HttpServletRequest request,
            HttpServletResponse response,
            Resource resource)
            throws IOException {
        try {
            long lastModified = resource.getLastModified();
            long headerValue = request.getDateHeader("If-Unmodified-Since");
            if (headerValue != -1) {
                if (lastModified >= (headerValue + 1000)) {
                    // The entity has not been modified since the date
                    // specified by the client. This is not an error case.
                    response.sendError(HttpServletResponse.SC_PRECONDITION_FAILED);
                    return false;
                }
            }
        } catch (IllegalArgumentException illegalArgument) {
            return true;
        }
        return true;
    }

    /**
     * Copy the contents of the specified input stream to the specified output
     * stream, and ensure that both streams are closed before returning (even in
     * the face of an exception).
     *
     * @param resource The cache entry for the source resource
     * @param is The input stream to read the source resource from
     * @param ostream The output stream to write to
     *
     * @exception IOException if an input/output error occurs
     */
    private void copy(Resource resource, ServletOutputStream ostream)
            throws IOException {
        IOException exception;

        try (InputStream stream = resource.openStream();
                BufferedInputStream bis = new BufferedInputStream(stream, input)) {
            // Copy the input stream to the output stream
            exception = copyRange(bis, ostream);
        }
        // Rethrow any exception that has occurred
        if (exception != null) {
            throw exception;
        }
    }

    /**
     * Copy the contents of the specified input stream to the specified output
     * stream, and ensure that both streams are closed before returning (even in
     * the face of an exception).
     *
     * @param resource The cache entry for the source resource
     * @param ostream The output stream to write to
     * @param range Range the client wanted to retrieve
     * @exception IOException if an input/output error occurs
     */
    private void copy(Resource resource, ServletOutputStream ostream,
            Range range) throws IOException {
        IOException exception;
        try (InputStream stream = resource.openStream();
                BufferedInputStream bis = new BufferedInputStream(stream, input)) {
            exception = copyRange(bis, ostream, range.start, range.end);
        }
        // Rethrow any exception that has occurred
        if (exception != null) {
            throw exception;
        }
    }

    /**
     * Copy the contents of the specified input stream to the specified output
     * stream, and ensure that both streams are closed before returning (even in
     * the face of an exception).
     *
     * @param resource The cache entry for the source resource
     * @param ostream The output stream to write to
     * @param ranges Enumeration of the ranges the client wanted to retrieve
     * @param contentType Content type of the resource
     * @exception IOException if an input/output error occurs
     */
    private void copy(Resource resource, ServletOutputStream ostream, Iterator<Range> ranges, String contentType)
            throws IOException {
        IOException exception = null;
        while ((exception == null) && (ranges.hasNext())) {
            try (InputStream stream = resource.openStream();
                    BufferedInputStream bis = new BufferedInputStream(stream, input);) {

                Range currentRange = ranges.next();
                // Writing MIME header.
                ostream.println();
                ostream.println("--" + MIME_SEPARATION);
                if (contentType != null) {
                    ostream.println("Content-Type: " + contentType);
                }
                ostream.println("Content-Range: bytes " + currentRange.start + "-" + currentRange.end + "/" + currentRange.length);
                ostream.println();
                // Printing content
                exception = copyRange(bis, ostream, currentRange.start,
                        currentRange.end);
            }
        }
        ostream.println();
        ostream.print("--" + MIME_SEPARATION + "--");
        // Rethrow any exception that has occurred
        if (exception != null) {
            throw exception;
        }
    }

    /**
     * Copy the contents of the specified input stream to the specified output
     * stream, and ensure that both streams are closed before returning (even in
     * the face of an exception).
     *
     * @param istream The input stream to read from
     * @param ostream The output stream to write to
     * @return Exception which occurred during processing
     */
    private IOException copyRange(InputStream istream, ServletOutputStream ostream) {
        // Copy the input stream to the output stream
        IOException exception = null;
        byte buffer[] = new byte[input];
        while (true) {
            try {
                int len = istream.read(buffer);
                if (len == -1) {
                    break;
                }
                ostream.write(buffer, 0, len);
            } catch (IOException e) {
                exception = e;
                break;
            }
        }
        return exception;
    }

    /**
     * Copy the contents of the specified input stream to the specified output
     * stream, and ensure that both streams are closed before returning (even in
     * the face of an exception).
     *
     * @param istream The input stream to read from
     * @param ostream The output stream to write to
     * @param start Start of the range which will be copied
     * @param end End of the range which will be copied
     * @return Exception which occurred during processing
     */
    private IOException copyRange(InputStream istream,
            ServletOutputStream ostream,
            long start, long end) {
        LOG.trace("Serving bytes:{}-{}", start, end);

        long skipped;
        try {
            skipped = istream.skip(start);
        } catch (IOException e) {
            return e;
        }
        if (skipped < start) {
            return new IOException("Only skipped [" + skipped + "] bytes when [" + start + "] were requested");
        }
        IOException exception = null;
        long bytesToRead = end - start + 1;
        byte buffer[] = new byte[input];
        int len = buffer.length;
        while ((bytesToRead > 0) && (len > 0)) {
            try {
                len = istream.read(buffer);
                if (bytesToRead >= len) {
                    ostream.write(buffer, 0, len);
                    bytesToRead -= len;
                } else {
                    ostream.write(buffer, 0, (int) bytesToRead);
                    bytesToRead = 0;
                }
            } catch (IOException e) {
                exception = e;
                len = -1;
            }
        }
        return exception;
    }

    private static class Range {

        public long start;
        public long end;
        public long length;

        /**
         * Validate range.
         */
        public boolean validate() {
            if (end >= length) {
                end = length - 1;
            }
            return (start >= 0) && (end >= 0) && (start <= end) && (length > 0);
        }
    }
}
