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

import com.github.zhanhb.judge.exception.EntityNotExistException;
import com.github.zhanhb.judge.model.Submission;
import com.github.zhanhb.judge.repository.SubmissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author zhanhb
 */
@Controller
@RequestMapping("status")
public class SubmissionController extends BaseController implements Restful<Submission, Long> {

    @Autowired
    private SubmissionRepository repository;

    @Override
    public Submission view(Long search) {
        return repository.findById(search).orElseThrow(EntityNotExistException::new);
    }

    @Override
    public ModelAndView view(Long search, ModelMap model) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Page<Submission> list(@SortDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public ModelAndView findAll(Pageable pageable, ModelMap model) {
        return new ModelAndView("status/list");
    }

}
