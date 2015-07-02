package com.github.zhanhb.judge.web;

import com.github.zhanhb.judge.testenv.AbstractMockMvcTests;
import org.junit.Test;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

public class XmlTestControllerTest extends AbstractMockMvcTests {

    private String encoding = "ISO-8859-1";

    @Test
    public void test() throws Exception {
        mockMvc.perform(get("/test").accept(APPLICATION_JSON))
                .andExpect(
                        content().contentType(APPLICATION_JSON_VALUE)
                ).andExpect(content().encoding(encoding))
                .andExpect(content().string("{\"hello\":\"world\"}"));
    }
}
