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
package com.github.zhanhb.judge.testenv;

import java.sql.Connection;
import java.sql.SQLException;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.stereotype.Component;

/**
 *
 * @author zhanhb
 */
@Component
@Slf4j
public class DatabaseInfo {

    /**
     *
     * @see DataSourceAutoConfiguration.JdbcTemplateConfiguration#jdbcTemplate()
     */
    @Autowired
    private JdbcOperations jdbcOperations;

    @PostConstruct
    public void init() {
        log.info("product = " + jdbcOperations.execute((Connection connection)
                -> connection.getMetaData().getDatabaseProductName() + ' '
                + connection.getMetaData().getDatabaseProductVersion()));
    }

    @PreDestroy
    public void destroy() throws SQLException {
        log.info("database destroy");
    }

}
