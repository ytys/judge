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
package com.github.zhanhb.judge.web.form;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

/**
 *
 * @author zhanhb
 */
@Data
public class UserRegisterForm {

    @Pattern(regexp = "[a-zA-Z0-9]*+", message = "{UserRegisterForm.handle.invalid}")
    @Length(min = 5, max = 255)
    private String handle;
    @Pattern(regexp = "[\u0021-\u007e]*+", message = "{UserRegisterForm.password.invalid}")
    @Length(min = 6, max = 20)
    @NotNull
    private String password;
    @Email(regexp = Constants.Patterns.EMAIL_OR_EMPRTY)
    @Length(max = 255)
    @NotEmpty
    private String email;
    // will be validated in the controller
    private String rptPassword;
    @Length(max = 255)
    private String nickname;
    @Length(max = 255)
    private String school;
    @Length(max = 255)
    private String major;
    @Length(max = 255)
    private String realname;

}
