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
package com.github.zhanhb.judge.repository;

import com.github.zhanhb.judge.Application;
import com.github.zhanhb.judge.domain.Userprofile;
import com.github.zhanhb.judge.domain.UserprofileRole;
import java.util.Collection;
import java.util.Collections;
import lombok.extern.slf4j.Slf4j;
import static org.junit.Assert.assertEquals;
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
@Slf4j
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class UserprofileRoleRepositoryTest {

    @Autowired
    private UserprofileRoleRepository instance;

    /**
     * Test of findAllByUserprofile method, of class UserprofileRoleRepository.
     */
    @Test
    public void testFindAllByUserprofile() {
        log.info("findAllByUserprofile");
        Userprofile userprofile = null;
        Collection<UserprofileRole> expResult = Collections.emptyList();
        Collection<UserprofileRole> result = instance.findAllByUserprofile(userprofile);
        assertEquals(expResult, result);
    }

}
