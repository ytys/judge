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

import org.springframework.data.domain.Pageable;
import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.TEXT_HTML_VALUE;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author zhanhb
 * @param <T>
 * @param <SEARCH>
 */
public interface Restful<T, SEARCH> {

    @RequestMapping(value = "{search}", produces = {TEXT_HTML_VALUE, ALL_VALUE})
    ModelAndView view(SEARCH search, ModelMap model);

    @RequestMapping(produces = {TEXT_HTML_VALUE, ALL_VALUE})
    ModelAndView findAll(Pageable pageable, ModelMap model);

}
