package com.github.zhanhb.judge.web;

import com.github.zhanhb.judge.Application;
import com.github.zhanhb.judge.domain.Userprofile;
import com.github.zhanhb.judge.repository.UserprofileRepository;
import com.github.zhanhb.judge.testenv.AbstractMockMvcTests;
import javax.annotation.Resource;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;
import org.springframework.web.context.WebApplicationContext;

// spring security, here is no controller, so ignored
@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class LoginControllerTest extends AbstractMockMvcTests {

    private static final String HANDLE = "testaccount2";
    private static final String PASSWORD = "admin";
    private static final String SHA = "d033e22ae348aeb5660fc2140aec35850c4da997";
    private static final String MAIL = "testemail2@example.com";

    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;

    @Resource
    private UserprofileRepository userprofiles;
    private Userprofile userprofile;

    @Before
    public void setUp() throws Exception {
        mockMvc = webAppContextSetup(wac).alwaysExpect(status().isOk()).build();
        userprofile = userprofiles.findByHandleIgnoreCase(HANDLE).orElseGet(
                () -> userprofiles.save(
                        Userprofile
                        .builder()
                        .handle(HANDLE)
                        .password(SHA)
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
        mockMvc.perform(post("/login").param("login", HANDLE).param("password", PASSWORD))
                .andExpect(view().name("success"));
    }

    @Test
    public void testLoginWithEmail() throws Exception {
        mockMvc.perform(post("/login").param("login", MAIL).param("password", PASSWORD))
                .andExpect(view().name("success"));
    }

    @Test
    public void testLoginRaw() throws Exception {
        mockMvc.perform(post("/login").param("login", MAIL).param("password", SHA))
                .andExpect(view().name("login"));
    }

    @Test
    public void testLoginFailed() throws Exception {
        mockMvc.perform(post("/login").param("login", "zhanhb8@qq.com").param("password", PASSWORD + 1))
                .andExpect(view().name("login"));
    }

}
