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

import com.github.zhanhb.judge.util.RequireServlet30;
import javax.servlet.ServletRegistration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;

/**
 *
 * @author zhanhb
 */
@Configuration
public class UploadConfig {

    /**
     * we declare a standard multipart resolver, we should configuration it.
     *
     * @return the multipart resolver
     * @see
     * ServletInitializer#customizeRegistration(ServletRegistration.Dynamic)
     */
    @Autowired
    @Bean(name = "multipartResolver")
    @RequireServlet30
    public MultipartResolver multipartResolver() {
        /* commons-fileupload item will not have parameters, here we use the standard one */
        return new StandardServletMultipartResolver();
    }

}
