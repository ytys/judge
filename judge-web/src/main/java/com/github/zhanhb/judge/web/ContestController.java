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

import com.github.zhanhb.judge.exception.ContestNotExistException;
import com.github.zhanhb.judge.domain.Contest;
import com.github.zhanhb.judge.repository.ContestRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author zhanhb
 */
@Controller
@RequestMapping("contests")
@Slf4j
public class ContestController extends BaseController implements Restful<Contest, String> {

    @Autowired
    private ContestRepository repository;

    @Override
    @SuppressWarnings("ThrowableInstanceNotThrown")
    public ModelAndView view(@PathVariable("search") String search, ModelMap model) {
        model.addAttribute("contest", repository.findByNameIgnoreCase(search).orElseThrow(() -> new ContestNotExistException(search)));
        return new ModelAndView("contests/view", model);
    }

    @Override
    public ModelAndView findAll(Pageable pageable, ModelMap model) {
        return new ModelAndView("contests/list", model);
    }

}
