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

import com.github.zhanhb.judge.config.SecurityConfig;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_XML_VALUE;
import static org.springframework.http.MediaType.TEXT_HTML_VALUE;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

/**
 *
 * @author zhanhb
 */
@Controller
public class LoginController extends BaseController {

    @RequestMapping(value = "login", method = RequestMethod.GET, produces = TEXT_HTML_VALUE)
    public String login(WebRequest request, Model model) {
        String url = Optional.ofNullable(request.getParameter(SecurityConfig.TARGET_URL_PARAMETER))
                .orElseGet(
                        () -> Optional.ofNullable(request.getHeader("referer"))
                        .orElseGet(request::getContextPath)
                );
        model.addAttribute(SecurityConfig.TARGET_URL_PARAMETER, url);
        return "login";
    }

    @RequestMapping(value = "login", method = RequestMethod.GET, produces = {APPLICATION_JSON_VALUE, APPLICATION_XML_VALUE})
    @ResponseBody
    public ResponseEntity<Map<String, ? extends Object>> login() {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(Collections.singletonMap("message", "not login"));
    }

    @RequestMapping(value = "password", method = RequestMethod.GET, produces = TEXT_HTML_VALUE)
    public String password() {
        return "password";
    }

}
