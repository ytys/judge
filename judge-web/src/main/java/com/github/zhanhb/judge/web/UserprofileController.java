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

import com.github.zhanhb.judge.domain.Userprofile;
import com.github.zhanhb.judge.exception.UserprofileNotExistException;
import com.github.zhanhb.judge.repository.UserprofileRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author zhanhb
 */
@Controller
@RequestMapping("users")
@Slf4j
public class UserprofileController extends BaseController {

    @Autowired
    private UserprofileRepository repository;

    @RequestMapping("{search}")
    @SuppressWarnings("ThrowableInstanceNeverThrown")
    public Userprofile view(String search) {
        return repository.findByHandleIgnoreCase(search).orElseThrow(() -> new UserprofileNotExistException(search));
    }

    @RequestMapping
    public Page<Userprofile> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

}
