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

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.h2.util.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author zhanhb
 */
@Controller
public class TestController {

    @RequestMapping(value = "upload", consumes = "multipart/*", method = RequestMethod.POST)
    public void upload(@RequestParam("file") MultipartFile file, Model model,
            HttpServletResponse response) throws IOException {
        response.setContentType(file.getContentType());
        try (InputStream inputStream = file.getInputStream()) {
            IOUtils.copy(inputStream, response.getOutputStream());
        }
    }

    @RequestMapping(value = "tt/{id}")
    public void show(HttpServletRequest request, HttpServletResponse response,
            @PathVariable String id) throws IOException {
        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println(id);
        }
    }
}
