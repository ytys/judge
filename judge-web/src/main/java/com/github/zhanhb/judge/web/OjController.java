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

import com.github.zhanhb.judge.model.Contest;
import com.github.zhanhb.judge.model.enums.ContestType;
import com.github.zhanhb.judge.repository.ContestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author zhanhb
 */
@Controller
@RequestMapping("oj")
public class OjController {

    @Autowired
    private ContestRepository repository;

    @RequestMapping
    @ResponseBody
    public PagedResources<?> oj(Pageable pageable, PagedResourcesAssembler<Contest> assembler) {
        Page<Contest> page = repository.findAllByType(ContestType.OJ, pageable);
        return assembler.toResource(page);
    }

}
