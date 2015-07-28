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

import com.github.zhanhb.judge.domain.ContestProblem;
import com.github.zhanhb.judge.repository.ContestProblemRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author zhanhb
 */
@Controller
@RequestMapping("contests")
@Slf4j
public class ContestProblemController extends BaseController {

    @Autowired
    private ContestProblemRepository contestProblems;

    @RequestMapping("{contestName}")
    public String home(RedirectAttributes redirectAttrs) {
        return "redirect:/contest/{contestName}/problems";
    }

    @RequestMapping("{contestName}/problems")
    public String problems(@PathVariable("contestName") String contestName, Pageable pageable, Model model) {
        model.addAttribute("problems", contestProblems.findAllByContestName(contestName, pageable));
        return "contest/problems";
    }

    @RequestMapping("{contestName}/problem/{order:\\d+}")
    public String view(@PathVariable("contestName") String contestName, @PathVariable("order") long order, Model model) {
        ContestProblem contestProblem = contestProblems.findOneByContestNameAndContestOrder(contestName, order);
        model.addAttribute("problem", contestProblem);
        return "contests/problem/view";
    }

}
