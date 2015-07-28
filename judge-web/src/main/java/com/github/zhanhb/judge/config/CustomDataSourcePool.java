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
package com.github.zhanhb.judge.config;

import com.github.zhanhb.judge.util.Standalone;
import com.jolbox.bonecp.BoneCPDataSource;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author zhanhb
 */
@Configuration
@Standalone(optional = true)
public class CustomDataSourcePool {

    @Autowired
    private DataSourceProperties p;

    @Bean
    public DataSource dataSource() {
        String url = p.getUrl();
        String username = p.getUsername();
        String password = p.getPassword();
        BoneCPDataSource bonecp = new BoneCPDataSource();
        bonecp.setJdbcUrl(url);
        bonecp.setUsername(username);
        bonecp.setPassword(password);
        bonecp.setLogStatementsEnabled(true);
        bonecp.setConnectionTestStatement("select 1");
        return bonecp;
    }

}
