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
import com.github.zhanhb.judge.model.ContestUserprofileStatistics;
import com.github.zhanhb.judge.repository.ContestUserprofileStatisticsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_XML_VALUE;
import static org.springframework.http.MediaType.TEXT_HTML_VALUE;
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
@RequestMapping("/contest/{contest}/cssc")
public class ContestUserprofileStatisticsController extends BaseController {

    @Autowired
    private ContestUserprofileStatisticsRepository repository;

    @RequestMapping(value = "{handle}", produces = {APPLICATION_XML_VALUE, APPLICATION_JSON_VALUE})
    @SuppressWarnings("ThrowableInstanceNotThrown")
    public ContestUserprofileStatistics view(
            @PathVariable("contest") String contest,
            @PathVariable("handle") String handle) {
        return repository.findByContestNameAndUserprofileHandle(contest, handle)
                .orElseThrow(() -> new ContestNotExistException(contest));
    }

    @RequestMapping(value = "{search}", produces = {TEXT_HTML_VALUE, ALL_VALUE})
    public ModelAndView view(@PathVariable("contest") String contest, @PathVariable("search") String search, ModelMap model) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @RequestMapping(produces = {APPLICATION_XML_VALUE, APPLICATION_JSON_VALUE})
    public Page<ContestUserprofileStatistics> list(String contest, Pageable pageable) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @RequestMapping(produces = {TEXT_HTML_VALUE, ALL_VALUE})
    public ModelAndView findAll(String contest, Pageable pageable, ModelMap model) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
