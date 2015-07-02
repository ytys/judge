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
package com.github.zhanhb.judge.auditing;

import com.github.zhanhb.judge.Application;
import com.github.zhanhb.judge.audit.UserAuditorAware;
import com.github.zhanhb.judge.model.Userprofile;
import static org.junit.Assert.assertNull;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 *
 * @author zhanhb
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class UserAuditorAwareTest {

    @Autowired
    private UserAuditorAware instance;

    /**
     * Test of getCurrentAuditor method, of class UserAuditorAware.
     */
    @Test
    public void testGetCurrentAuditor() {
        System.out.println("getCurrentAuditor");
        Userprofile result = instance.getCurrentAuditor();
        assertNull(result);
    }

}
