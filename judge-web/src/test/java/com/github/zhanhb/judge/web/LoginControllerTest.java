package com.github.zhanhb.judge.web;

import com.github.zhanhb.judge.model.Userprofile;
import com.github.zhanhb.judge.repository.UserprofileRepository;
import com.github.zhanhb.judge.testenv.AbstractMockMvcTests;
import javax.annotation.Resource;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

// spring security, here is no controller, so ignored
@Ignore
public class LoginControllerTest extends AbstractMockMvcTests {

    private static final String handle = "testaccount2";
    private static final String password = "admin";
    private static final String sha = "d033e22ae348aeb5660fc2140aec35850c4da997";
    private static final String email = "testemail2@example.com";

    @Resource
    private UserprofileRepository userprofileRepository;

    @Before
    public void setUp() throws Exception {
        userprofileRepository.findByHandleIgnoreCase(handle).orElseGet(
                () -> userprofileRepository.save(
                        Userprofile
                        .builder()
                        .handle(handle)
                        .password(sha)
                        .nickname("nick")
                        .school("school")
                        .major("major")
                        .build()
                )
        );
    }

    @Test
    public void testLoginWithHandle() throws Exception {
        mockMvc.perform(post("/login").param("login", handle).param("password", password))
                .andExpect(view().name("success"));
    }

    @Test
    public void testLoginWithEmail() throws Exception {
        mockMvc.perform(post("/login").param("login", email).param("password", password))
                .andExpect(view().name("success"));
    }

    @Test
    public void testLoginRaw() throws Exception {
        mockMvc.perform(post("/login").param("login", email).param("password", sha))
                .andExpect(view().name("login")); // TODO password too long
    }

    @Test
    public void testLoginFailed() throws Exception {
        mockMvc.perform(post("/login").param("login", "zhanhb8@qq.com").param("password", password + 1))
                .andExpect(view().name("login"));
    }

}
