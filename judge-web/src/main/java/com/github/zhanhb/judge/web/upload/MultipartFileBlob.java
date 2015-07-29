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
package com.github.zhanhb.judge.web.upload;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author zhanhb
 */
@SuppressWarnings("FinalClass")
public final class MultipartFileBlob implements Blob {

    private final MultipartFile file;

    public MultipartFileBlob(MultipartFile file) {
        this.file = file;
    }

    @Override
    public long length() throws SQLException {
        return file.getSize();
    }

    @Override
    public byte[] getBytes(long pos, int length) throws SQLException {
        throw new SQLFeatureNotSupportedException("Not supported yet.");
    }

    @Override
    public InputStream getBinaryStream() throws SQLException {
        try {
            return file.getInputStream();
        } catch (IOException ex) {
            throw new SQLException(ex);
        }
    }

    @Override
    public long position(byte[] pattern, long start) throws SQLException {
        throw new SQLFeatureNotSupportedException("Not supported yet.");
    }

    @Override
    public long position(Blob pattern, long start) throws SQLException {
        throw new SQLFeatureNotSupportedException("Not supported yet.");
    }

    @Override
    public int setBytes(long pos, byte[] bytes) throws SQLException {
        throw new SQLFeatureNotSupportedException("Not supported yet.");
    }

    @Override
    public int setBytes(long pos, byte[] bytes, int offset, int len) throws SQLException {
        throw new SQLFeatureNotSupportedException("Not supported yet.");
    }

    @Override
    public OutputStream setBinaryStream(long pos) throws SQLException {
        throw new SQLFeatureNotSupportedException("Not supported yet.");
    }

    @Override
    public void truncate(long len) throws SQLException {
        throw new SQLFeatureNotSupportedException("Not supported yet.");
    }

    @Override
    public void free() {
    }

    @Override
    public InputStream getBinaryStream(long pos, long length) throws SQLException {
        throw new SQLFeatureNotSupportedException("Not supported yet.");
    }

}
