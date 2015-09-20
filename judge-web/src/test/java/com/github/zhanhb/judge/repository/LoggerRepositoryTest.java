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

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.util.ContextSelectorStaticBinder;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
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
public class LoggerRepositoryTest {

    private final LoggerRepository instance = new LoggerRepository();

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
     * Test of findAll method, of class LoggerRepository.
     */
    @Test
    public void testFindAll_0args() {
        log.info("findAll");
        Iterable<Logger> result = instance.findAll();
        assertNotNull(result);
    }

    /**
     * Test of findAll method, of class LoggerRepository.
     */
    @Test
    public void testFindAll_Pageable() {
        log.info("findAll");
        Pageable pageable = new PageRequest(0, 20, Sort.Direction.DESC, "level", "name");
        Page<Logger> result = instance.findAll(pageable);
        assertNotNull(result);
    }

    /**
     * Test of save method, of class LoggerRepository.
     */
    @Test
    public void testSave() {
        String name = "";
        String level = "";
        instance.save(name, level);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSaveRootNull() {
        String name = Logger.ROOT_LOGGER_NAME;
        String level = null;
        instance.save(name, level);
    }

    @Test
    public void testSaveRoot() {
        String name = Logger.ROOT_LOGGER_NAME;
        Level level = instance.findOne(name).get().getLevel();
        try {
            instance.save(name, "WARN");
        } finally {
            instance.save(name, level.toString());
        }
    }

    @Test
    public void testDefaultIsByName() {
        Pageable pageable = new PageRequest(0, 20000);
        Pageable pageable2 = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), new Sort("name"));
        List<Logger> content = instance.findAll(pageable).getContent();
        List<Logger> content1 = instance.findAll(pageable2).getContent();
        assertEquals(content, content1);
    }

    @Test
    public void testNoProperty() {
        PageRequest pageRequest = new PageRequest(0, 20, Sort.Direction.DESC, "testKey");
        try {
            instance.findAll(pageRequest);
            fail("should throw a RuntimeException");
        } catch (RuntimeException ex) {
            assertThat(ex.getMessage(), containsString("No property"));
        }
    }
}
