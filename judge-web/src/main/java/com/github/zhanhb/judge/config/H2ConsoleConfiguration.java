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
package com.github.zhanhb.judge.config;

import com.github.zhanhb.judge.util.Standalone;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author zhanhb
 */
@Configuration
@Slf4j
@Standalone
public class H2ConsoleConfiguration {

    public static final String CONSOLE_URL_PREFIX = "/console/";

    @Bean
    public ServletRegistrationBean h2Console() {
        log.debug("Initialize H2 console");
        ServletRegistrationBean bean = new ServletRegistrationBean(new org.h2.server.web.WebServlet(), CONSOLE_URL_PREFIX + "*");
        // don't load properties from file.
        bean.addInitParameter("-properties", "null");
        return bean;
    }

}