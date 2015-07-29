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

import com.github.zhanhb.judge.domain.Role;
import com.github.zhanhb.judge.domain.Userprofile;
import com.github.zhanhb.judge.domain.UserprofileRole;
import com.github.zhanhb.judge.repository.UserprofileRepository;
import com.github.zhanhb.judge.repository.UserprofileRoleRepository;
import com.github.zhanhb.judge.service.Roles;
import com.github.zhanhb.judge.util.Strings;
import com.github.zhanhb.judge.web.form.UserRegisterForm;
import com.google.common.base.Objects;
import java.util.function.Consumer;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import static org.springframework.http.MediaType.TEXT_HTML_VALUE;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author zhanhb
 */
@Controller
public class RegisterController extends BaseController {

    @Autowired
    private UserprofileRepository repository;
    @Autowired
    private UserprofileRoleRepository urr;
    @Autowired
    private Roles roles;
    @Autowired
    private PasswordEncoder encoder;

    @ModelAttribute("userRegisterForm")
    public UserRegisterForm createUserRegisterForm() {
        return new UserRegisterForm();
    }

    @RequestMapping(value = "register", method = RequestMethod.GET, produces = TEXT_HTML_VALUE)
    public String registerView() {
        return "register";
    }

    private Consumer<Object> rejectValue(BindingResult result, String field, String errorCode) {
        return (__) -> result.rejectValue(field, errorCode);
    }

    @RequestMapping(value = "register", method = RequestMethod.POST, produces = TEXT_HTML_VALUE)
    @Transactional
    public String register(@Valid UserRegisterForm userRegisterForm, BindingResult result,
            @ModelAttribute("ajaxRequest") boolean ajaxRequest,
            RedirectAttributes redirectAttrs) {

        Consumer<Object> rptPasswordAction = rejectValue(result, "rptPassword", "NotSame");
        if (!Objects.equal(userRegisterForm.getPassword(), userRegisterForm.getRptPassword())) {
            rptPasswordAction.accept(this);
        }
        Consumer<Object> handleAction = rejectValue(result, "handle", "Exists");
        try {
            repository.findByHandleIgnoreCase(userRegisterForm.getHandle()).ifPresent(handleAction);
        } catch (IncorrectResultSizeDataAccessException ex) {
            handleAction.accept(ex);
        }
        Consumer<Object> emailAction = rejectValue(result, "email", "Exists");
        try {
            repository.findByEmail(userRegisterForm.getEmail()).ifPresent(emailAction);
        } catch (IncorrectResultSizeDataAccessException ex) {
            emailAction.accept(ex);
        }

        if (result.hasErrors()) {
            return "register";
        }

        Userprofile userprofile = repository.save(Userprofile
                .builder()
                .handle(userRegisterForm.getHandle())
                .password(encoder.encode(userRegisterForm.getPassword()))
                .email(Strings.trimToNull(userRegisterForm.getEmail()))
                .school(Strings.trimToNull(userRegisterForm.getSchool()))
                .major(Strings.trimToNull(userRegisterForm.getMajor()))
                .nickname(Strings.trimToNull(userRegisterForm.getNickname()))
                .build()
        );
        Role roleUser = roles.user();
        urr.findByUserprofileAndRole(userprofile, roleUser).orElseGet(
                () -> urr.save(UserprofileRole.builder()
                        .userprofile(userprofile)
                        .role(roleUser).build())
        );
        String message = "success";
        if (ajaxRequest) {
            return null;
        }
        redirectAttrs.addFlashAttribute("message", message);
        return "redirect:/";
    }

}
