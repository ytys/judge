package com.github.zhanhb.judge.web;

import com.github.zhanhb.judge.Application;
import com.github.zhanhb.judge.domain.Userprofile;
import com.github.zhanhb.judge.repository.UserprofileRepository;
import org.junit.After;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;
import org.springframework.web.context.WebApplicationContext;

@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class LoginControllerTest {

    private static final String HANDLE = "testaccount2";
    private static final String PASSWORD = "admin";
    private static final String SHA = "d033e22ae348aeb5660fc2140aec35850c4da997";
    private static final String EMAIL = "testemail2@example.com";

    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;

    @Autowired
    private UserprofileRepository userprofiles;
    private Userprofile userprofile;
    @Autowired
    private FilterChainProxy filterChain;

    @Before
    public void setUp() throws Exception {
        mockMvc = webAppContextSetup(wac)
                .addFilter(filterChain)
                .build();
        userprofile = userprofiles.findByHandleIgnoreCase(HANDLE).orElseGet(
                () -> userprofiles.save(
                        Userprofile
                        .builder()
                        .handle(HANDLE)
                        .password(SHA)
                        .email(EMAIL)
                        .nickname("nick")
                        .school("school")
                        .major("major")
                        .build()
                )
        );
    }

    @After
    public void tearDown() {
        userprofiles.delete(userprofile);
    }

    @Test
    public void testLoginWithHandle() throws Exception {
        ResultActions resultActions = mockMvc.perform(post("/login").param("login", HANDLE).param("password", PASSWORD));
        resultActions.andDo(print()).andExpect(status().isFound());
        Throwable throwable = getSpringSecurityLastException(resultActions);
        assertNull(throwable);
    }

    @Test
    public void testLoginWithEmail() throws Exception {
        ResultActions resultActions = mockMvc.perform(post("/login").param("login", EMAIL).param("password", PASSWORD));
        resultActions.andDo(print()).andExpect(status().isFound());
        Throwable throwable = getSpringSecurityLastException(resultActions);
        assertNull(throwable);
    }

    @Test
    public void testLoginRaw() throws Exception {
        ResultActions resultActions = mockMvc.perform(post("/login").param("login", EMAIL).param("password", SHA));
        resultActions.andDo(print()).andExpect(status().isFound());
        Throwable throwable = getSpringSecurityLastException(resultActions);
        // login failed
        assertNotNull(throwable);
    }

    @Test
    public void testLoginFailed() throws Exception {
        ResultActions resultActions = mockMvc.perform(post("/login").param("login", "zhanhb8@qq.com").param("password", PASSWORD + 1));
        resultActions.andDo(print()).andExpect(status().isFound());
        Throwable throwable = getSpringSecurityLastException(resultActions);
        // login failed
        assertNotNull(throwable);
    }

    private Throwable getSpringSecurityLastException(ResultActions resultActions) {
        return (Throwable) resultActions.andReturn().getRequest().getSession().getAttribute("SPRING_SECURITY_LAST_EXCEPTION");
    }

}
