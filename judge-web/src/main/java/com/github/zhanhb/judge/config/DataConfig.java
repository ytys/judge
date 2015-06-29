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

import com.github.zhanhb.judge.model.ModelInfo;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.ResourceBundle;
import javax.persistence.EntityManagerFactory;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

/**
 *
 * @author zhanhb
 */
@Configuration
public class DataConfig {

    @Bean(name = "entityManagerFactory", destroyMethod = "destroy")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setJpaProperties(jpaProperties());
        factory.setPackagesToScan(ModelInfo.class.getPackage().getName());
        factory.setPersistenceProviderClass(HibernatePersistenceProvider.class);
        return factory;
    }

    private Properties jpaProperties() {
        MissingResourceException exception = null;
        for (String databasePropertiesLocation : new String[]{"db-test", "db"}) {
            try {
                Properties properties = new Properties();
                ResourceBundle bundle = ResourceBundle.getBundle(databasePropertiesLocation);
                bundle.keySet().stream().forEach(key -> properties.put(key, bundle.getObject(key)));
                return properties;
            } catch (MissingResourceException ex) {
                exception = ex;
            }
        }
        throw exception;
    }

    @Autowired
    @Bean(name = "transactionManager")
    public JpaTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager tm = new JpaTransactionManager();
        tm.setEntityManagerFactory(entityManagerFactory);
        return tm;
    }

}
