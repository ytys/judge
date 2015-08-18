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
package com.github.zhanhb.judge.service;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.util.ContextSelectorStaticBinder;
import lombok.extern.slf4j.Slf4j;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 *
 * @author zhanhb
 */
@Slf4j
public class LoggersTest {

    private final Loggers instance = new Loggers();

    @Before
    public void setUp() throws Exception {
        try {
            ContextSelectorStaticBinder.getSingleton().init(null, null);
            fail("StaticBinder.singleton not initlized yet.");
        } catch (IllegalAccessException ex) {
            // this is the good case
        }
    }

    /**
     * Test of findAll method, of class Loggers.
     */
    @Test
    public void testFindAll_0args() {
        log.info("findAll");
        Iterable<Logger> result = instance.findAll();
        assertNotNull(result);
    }

    /**
     * Test of findAll method, of class Loggers.
     */
    @Test
    public void testFindAll_Pageable() {
        log.info("findAll");
        Pageable pageable = new PageRequest(0, 20, Sort.Direction.DESC, "level", "name");
        Page<Logger> result = instance.findAll(pageable);
        assertNotNull(result);
    }

    /**
     * Test of save method, of class Loggers.
     */
    @Test
    public void testSave() {
        String name = "";
        String level = "";
        instance.save(name, level);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSaveRootNull() {
        String name = "ROOT";
        String level = null;
        instance.save(name, level);
    }

    @Test
    public void testSaveRoot() {
        String name = "ROOT";
        Level level = instance.findOne(name).get().getLevel();
        try {
            instance.save(name, "WARN");
        } finally {
            instance.save(name, level.toString());
        }
    }

}
