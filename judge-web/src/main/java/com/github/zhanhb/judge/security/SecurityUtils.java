package com.github.zhanhb.judge.security;

import com.github.zhanhb.judge.audit.CustomUserDetails;
import com.github.zhanhb.judge.model.Userprofile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Utility class for Spring Security.
 */
@Slf4j
public final class SecurityUtils {

    private SecurityUtils() {
    }

    /**
     * Get the login of the current user.
     *
     * @return
     */
    public static Userprofile getCurrentLogin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof CustomUserDetails) {
            if (log.isDebugEnabled()) {
                log.debug(((UserDetails) principal).getUsername());
            }
            return ((CustomUserDetails) principal).getUserprofile();
        } else if (log.isDebugEnabled()) {
            log.debug(String.valueOf(principal));
        }

        return null;
    }

    /**
     * Check if a user is authenticated.
     *
     * @return true if the user is authenticated, false otherwise
     */
    public static boolean isAuthenticated() {
        return getCurrentLogin() != null;
    }

}
