package com.github.zhanhb.judge.core;

import static org.junit.Assert.fail;
import org.junit.Test;

/**
 *
 * @author zhanhb
 */
public class JudgerTest {

    /**
     * Test of shutdown method, of class Judger.
     */
    @Test
    public void testShutdownIse() {
        Judger instance = new Judger();
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
