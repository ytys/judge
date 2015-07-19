/*
 * Copyright 2015 Pivotal Software, Inc..
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
import com.github.zhanhb.judge.repository.RoleRepository;
import com.github.zhanhb.judge.security.AuthoritiesConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author zhanhb
 */
@Component
public class Roles {

    @Autowired
    private RoleRepository repository;

    private Role getRole(String roleName) {
        return repository.findByName(roleName)
                .orElseGet(
                        () -> repository.save(
                                Role.builder()
                                .name(roleName)
                                .build()
                        ));
    }

    public Role user() {
        return getRole(AuthoritiesConstants.USER);
    }

    public Role anonymous() {
        return getRole(AuthoritiesConstants.ANONYMOUS);
    }

    public Role admin() {
        return getRole(AuthoritiesConstants.ADMIN);
    }

}
