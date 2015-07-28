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
package com.github.zhanhb.judge.audit;

import com.github.zhanhb.judge.domain.Userprofile;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

/**
 *
 * @author zhanhb
 */
public class CustomUserDetails implements UserDetails, Serializable {

    private static final long serialVersionUID = 1L;
    private final Userprofile userprofile;
    private final List<GrantedAuthority> authorities;

    public CustomUserDetails(Userprofile userprofile, String[] authorities) {
        this.userprofile = Objects.requireNonNull(userprofile, "userprofile");
        this.authorities = AuthorityUtils.createAuthorityList(authorities);
    }

    public Userprofile getUserprofile() {
        return userprofile;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.unmodifiableList(authorities);
    }

    @Override
    public String getPassword() {
        return userprofile.getPassword();
    }

    @Override
    public String getUsername() {
        return userprofile.getHandle();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return !userprofile.isDisabled();
    }

}
