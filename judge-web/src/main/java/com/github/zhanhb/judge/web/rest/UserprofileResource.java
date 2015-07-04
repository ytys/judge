package com.github.zhanhb.judge.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.github.zhanhb.judge.model.Userprofile;
import com.github.zhanhb.judge.repository.UserprofileRepository;
import com.github.zhanhb.judge.security.AuthoritiesConstants;
import javax.annotation.security.RolesAllowed;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing users.
 */
@RestController
@RequestMapping("/app")
@Slf4j
public class UserprofileResource {

    @Autowired
    private UserprofileRepository userRepository;

    /**
     * GET /rest/users/:login -> get the "login" user.
     */
    @RequestMapping(value = "/rest/users/{login}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @RolesAllowed(AuthoritiesConstants.ADMIN)
    ResponseEntity<Userprofile> getUserprofile(@PathVariable String login) {
        log.debug("REST request to get User : {}", login);
        return userRepository.findByHandleIgnoreCase(login)
                .map(user -> new ResponseEntity<>(user, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
