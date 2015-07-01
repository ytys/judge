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

import com.github.zhanhb.judge.exception.UserprofileNotExistException;
import com.github.zhanhb.judge.model.Userprofile;
import com.github.zhanhb.judge.repository.UserprofileRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author zhanhb
 */
@Controller
@RequestMapping("user")
@Slf4j
public class UserprofileController extends BaseController implements Restful<Userprofile, String> {

    @Autowired
    private UserprofileRepository repository;

    @Override
    @SuppressWarnings("ThrowableInstanceNeverThrown")
    public Userprofile view(String search) {
        return repository.findByHandleIgnoreCase(search).orElseThrow(() -> new UserprofileNotExistException(search));
    }

    @Override
    public ModelAndView view(String search, ModelMap model) {
        log.debug("handle = " + search);
        model.addAttribute("userprofile", view(search));
        return new ModelAndView("user/view", model);
    }

    @Override
    public Page<Userprofile> list(@PageableDefault(size = 50, sort = "id") Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public ModelAndView findAll(Pageable pageable, ModelMap model) {
        return new ModelAndView("user/list", model);
    }

}
