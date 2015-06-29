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
package com.github.zhanhb.judge.web.filter;

import com.github.zhanhb.judge.service.ContestService;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.util.Assert;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 *
 * @author zhanhb
 */
public class ContestFilter extends OncePerRequestFilter {

    /**
     *
     * with DelegatingFilterProxy, you can use @Autowired here, but it doesn't
     * take me easier to do the work
     *
     * @see org.springframework.web.filter.DelegatingFilterProxy
     */
    private ContestService service;

    @Override
    protected void initFilterBean() throws ServletException {
        WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
        service = wac.getBean(ContestService.class);
        Assert.notNull(service, "there is no bean ContestService present.");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
            FilterChain chain) throws ServletException, IOException {
        String servletPath = request.getServletPath();
        if (service.shouldGotoContest(servletPath)) {
            request.setAttribute("noContestPrefix", true);
            request.getRequestDispatcher("/contest" + servletPath).forward(request, response);
        } else {
            chain.doFilter(request, response);
        }
    }

}
