package com.github.zhanhb.judge.security;

import com.github.zhanhb.judge.model.Userprofile;
import com.github.zhanhb.judge.repository.UserprofileRepository;
import com.github.zhanhb.judge.util.Utility;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Utility class for Spring Security.
 */
@Slf4j
@Utility
public class SecurityUtils {

    @Autowired
    private UserprofileRepository repository;

    /**
     * Get the login of the current user.
     *
     * @return
     */
    public Userprofile getCurrentLogin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }

        Object principal = authentication.getPrincipal();
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
        return getCurrentLogin() != null;
    }

}
