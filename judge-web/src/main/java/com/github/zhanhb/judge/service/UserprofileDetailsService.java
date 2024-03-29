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
package com.github.zhanhb.judge.service;

import com.github.zhanhb.judge.audit.CustomUserDetails;
import com.github.zhanhb.judge.domain.Role;
import com.github.zhanhb.judge.domain.Userprofile;
import com.github.zhanhb.judge.domain.UserprofileRole;
import com.github.zhanhb.judge.repository.UserprofileRepository;
import com.github.zhanhb.judge.repository.UserprofileRoleRepository;
import java.util.Objects;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 *
 * @author zhanhb
 */
@Service
@Slf4j
public class UserprofileDetailsService implements UserDetailsService {

    @Autowired
    private UserprofileRepository userprofiles;
    @Autowired
    private UserprofileRoleRepository userprofileRoles;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Objects.requireNonNull(username, "username");
        Optional<Userprofile> optional = username.contains("@")
                ? userprofiles.findByEmail(username)
                : userprofiles.findByHandleIgnoreCase(username);

        @SuppressWarnings("ThrowableInstanceNotThrown")
        Userprofile userprofile = optional.orElseThrow(() -> new UsernameNotFoundException(username));

        return new CustomUserDetails(userprofile,
                userprofileRoles.findAllByUserprofile(userprofile)
                .stream()
                .map(UserprofileRole::getRole)
                .map(Role::getName)
                .toArray(String[]::new));
    }

}
