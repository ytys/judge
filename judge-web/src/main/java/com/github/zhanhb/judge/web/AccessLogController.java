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

import com.github.zhanhb.judge.model.AccessLog;
import com.github.zhanhb.judge.repository.AccessLogRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author zhanhb
 */
@Controller
@RequestMapping("access")
@Slf4j
public class AccessLogController extends BaseController {

    @Autowired
    private AccessLogRepository repository;

    @RequestMapping("user/{handle}")
    @ResponseBody
    public ResponseEntity<?> user(@PathVariable("handle") String handle, Pageable pageable,
            PagedResourcesAssembler<AccessLog> assembler) {
        Page<AccessLog> list = repository.findAllByUserprofileHandleIgnoreCase(handle, pageable);

        return ResponseEntity.ok(assembler.toResource(list));
    }

}
