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
package com.github.zhanhb.judge.security;

import com.github.zhanhb.judge.audit.CustomUserDetails;
import com.github.zhanhb.judge.domain.Userprofile;
import com.github.zhanhb.judge.repository.UserprofileRepository;
import com.github.zhanhb.judge.util.Utility;
import java.util.Objects;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Utility class for Spring Security.
 */
@Utility
public class SecurityUtils {

    /**
     * Configures the Spring Security {@link SecurityContext} to be
     * authenticated as the user with the given username and password as well as
     * the given granted authorities.
     *
     * @param username must not be {@literal null} or empty.
     * @param password must not be {@literal null} or empty.
     * @param roles the roles which the user have.
     * @return Authentication of specified username, password and roles
     */
    @Deprecated
    public static Authentication runAs(String username, String password, String... roles) {
        Objects.requireNonNull(username, "Username must not be null!");
        Objects.requireNonNull(password, "Password must not be null!");

        Authentication auth = new UsernamePasswordAuthenticationToken(username, password,
                AuthorityUtils.createAuthorityList(roles));
        SecurityContextHolder.getContext().setAuthentication(auth);
        return auth;
    }

    @Autowired
    private UserprofileRepository repository;

    /**
     * Get the login of the current user.
     *
     * @return current user profile.
     * @see com.github.zhanhb.judge.audit.UserAuditorAware
     */
    public Userprofile getCurrentUserprofile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof CustomUserDetails) {
            return ((CustomUserDetails) principal).getUserprofile();
        }

        String username = null;

        if (principal instanceof String) {
            username = (String) principal;
        } else if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        }
        return Optional.ofNullable(username).flatMap(repository::findByHandleIgnoreCase).orElse(null);
    }

    /**
     * Check if a user is authenticated.
     *
     * @return true if the user is authenticated, false otherwise
     */
    public boolean isAuthenticated() {
        return getCurrentUserprofile() != null;
    }

    public void runAs(Userprofile userprofile, Runnable action) {
        Objects.requireNonNull(userprofile);
        Objects.requireNonNull(action);
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication old = context.getAuthentication();
        Authentication auth = runAs(userprofile.getHandle(), userprofile.getPassword());
        try {
            action.run();
        } finally {
            if (context.getAuthentication() == auth) {
                context.setAuthentication(old);
            }
        }
    }

}
