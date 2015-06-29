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
package com.github.zhanhb.judge.web;

import com.github.zhanhb.judge.util.Standalone;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author zhanhb
 */
@Controller
@Standalone
public class QRcode extends BaseController {

    private final Map<String, String> contentTypeMap = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

    {
        contentTypeMap.put("jpg", "image/jpeg");
        contentTypeMap.put("jpeg", "image/jpeg");
        contentTypeMap.put("png", "image/png");
        contentTypeMap.put("gif", "image/gif");
    }

    @SuppressWarnings("AssignmentToMethodParameter")
    @RequestMapping(value = "qrcode", produces = "image/*")
    public void service(
            @RequestParam("width") int width,
            @RequestParam("height") int height,
            @RequestParam("text") String text,
            @RequestParam(value = "type", required = false, defaultValue = "png") String type,
            @RequestParam(value = "charset", required = false, defaultValue = "UTF-8") String charset,
            HttpServletResponse response) throws IOException {
        if (width < 1) {
            width = 1;
        }
        if (height < 1) {
            height = 1;
        }
        try {
            if (Math.multiplyExact(width, height) > 400_000) {
                width = 100;
                height = 100;
            }
        } catch (ArithmeticException ex) {
            width = 100;
            height = 100;
        }
        String contentType = contentTypeMap.get(type);
        if (contentType == null) {
            type = "png";
            contentType = contentTypeMap.get(type);
        }
        response.setContentType(contentType);

        try {
            BitMatrix matrix = new QRCodeWriter().encode(text, BarcodeFormat.QR_CODE, width, height, Collections.singletonMap(EncodeHintType.CHARACTER_SET, charset));
            MatrixToImageWriter.writeToStream(matrix, type, response.getOutputStream());
        } catch (WriterException ex) {
        }
    }
}
