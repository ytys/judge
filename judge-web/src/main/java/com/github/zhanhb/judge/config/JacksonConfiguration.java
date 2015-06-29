package com.github.zhanhb.judge.config;

import com.fasterxml.jackson.datatype.hibernate4.Hibernate4Module;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JSR310Module;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfiguration {

    @Bean
    public Hibernate4Module jacksonHibernate4Module() {
        return new Hibernate4Module();
    }

    @Bean
    public JSR310Module jacksonJSR310Module() {
        return new JSR310Module();
    }

    @Bean
    public Jdk8Module jacksonJdk8Module() {
        return new Jdk8Module();
    }

}
