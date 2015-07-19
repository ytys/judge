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

import com.github.zhanhb.judge.util.AjaxUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.context.request.WebRequest;

/**
 *
 * @author zhanhb
 */
@Slf4j
public abstract class BaseController {

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        log.debug("A binder for object: {}", binder.getObjectName());
    }

    @ModelAttribute(value = "ajaxRequest")
    public boolean ajaxRequest(WebRequest request) {
        return AjaxUtils.isAjaxRequest(request);
    }

}
