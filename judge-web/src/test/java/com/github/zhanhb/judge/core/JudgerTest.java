package com.github.zhanhb.judge.core;

import com.github.zhanhb.judge.Application;
import lombok.extern.slf4j.Slf4j;
import static org.junit.Assert.fail;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 *
 * @author zhanhb
 */
@RunWith(SpringJUnit4ClassRunner.class)
@Slf4j
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class JudgerTest {

    @Autowired
    private Judger instance;

    /**
     * Test of shutdown method, of class Judger.
     */
    @Test
    public void testShutdownIse() {
        instance.start();
        instance.shutdown();

        try {
            instance.start();
            fail();
        } catch (IllegalStateException ex) {
            // ok
        }
    }

}
