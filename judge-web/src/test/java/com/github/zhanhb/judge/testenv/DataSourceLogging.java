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

import javax.sql.DataSource;
import org.jdbcdslog.ConnectionPoolDataSourceProxy;
import org.jdbcdslog.JDBCDSLogException;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 *
 * @author zhanhb
 */
/*
 * enable logging for test scope.
 */
@AutoConfigureAfter(DataSourceAutoConfiguration.class)
@AutoConfigureBefore(LiquibaseAutoConfiguration.class)
@Configuration
public class DataSourceLogging {

    // bean name must different from orign data source, default bean name is the method name.
    @Bean
    @Primary
    public DataSource dataSourceProxy(DataSource dataSource) throws JDBCDSLogException {
        ConnectionPoolDataSourceProxy proxy = new ConnectionPoolDataSourceProxy();
        proxy.setTargetDSDirect(dataSource);
        return proxy;
    }

}
