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

import com.github.zhanhb.judge.model.Problem;
import com.github.zhanhb.judge.model.Userprofile;
import com.github.zhanhb.judge.repository.ProblemRepository;
import com.github.zhanhb.judge.repository.UserprofileRepository;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.h2.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.http.ResponseEntity;
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

    @Autowired
    private UserprofileRepository repository;
    @Autowired
    private ProblemRepository problemRepository;

    @RequestMapping(value = "users", method = RequestMethod.GET)
    ResponseEntity<PagedResources<Resource<Userprofile>>> persons(Pageable pageable,
            PagedResourcesAssembler<Userprofile> assembler) {

        return ResponseEntity.ok(assembler.toResource(repository.findAll(pageable)));
    }

    @RequestMapping(value = "problems", method = RequestMethod.GET)
    ResponseEntity<PagedResources<Resource<Problem>>> problems(@PageableDefault(size = 100) Pageable pageable,
            PagedResourcesAssembler<Problem> assembler) {

        return ResponseEntity.ok(assembler.toResource(problemRepository.findAll(pageable)));
    }

    @RequestMapping(value = "upload", consumes = "multipart/*")
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
