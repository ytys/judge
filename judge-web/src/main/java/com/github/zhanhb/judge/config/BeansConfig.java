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

import javax.servlet.ServletContext;
import org.hibernate.validator.HibernateValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.theme.CookieThemeResolver;

/**
 *
 * @author zhanhb
 */
@Configuration
public class BeansConfig {

    /* Resolves localized messages*.properties and application.properties files in the application to allow for internationalization.
     The messages*.properties files translate Roo generated messages which are part of the admin interface, the
     application.properties resource bundle localizes all application specific messages such as entity names and menu items. */
    @Bean(name = "messageSource")
    public ReloadableResourceBundleMessageSource messageSource() {
        ReloadableResourceBundleMessageSource bundle = new ReloadableResourceBundleMessageSource();
        bundle.setBasenames(new String[]{"classpath:messages", "classpath:org/springframework/security/messages"});
        bundle.setDefaultEncoding("UTF-8");
        bundle.setFallbackToSystemLocale(false);
        bundle.setUseCodeAsDefaultMessage(true);
        return bundle;
    }

    /* Store preferred language configuration in a cookie */
    @Bean(name = "localeResolver")
    public CookieLocaleResolver localeResolver(ServletContext container) {
        CookieLocaleResolver localeResolver = new CookieLocaleResolver();
        localeResolver.setCookieName("locale");
        localeResolver.setCookieMaxAge(3_600);
        localeResolver.setCookiePath(getCookiePath(container));
        return localeResolver;
    }

    @Bean(name = "themeResolver")
    public CookieThemeResolver themeResolver(ServletContext container) {
        CookieThemeResolver themeResolver = new CookieThemeResolver();
        themeResolver.setCookieName("theme");
        themeResolver.setCookiePath(getCookiePath(container));
        themeResolver.setDefaultThemeName("standard");
        return themeResolver;
    }

    private String getCookiePath(ServletContext container) {
        return container.getContextPath().replaceAll("/$", "") + '/';
    }

    @Autowired
    @Bean
    public LocalValidatorFactoryBean validator(MessageSource messageSource) {
        LocalValidatorFactoryBean factory = new LocalValidatorFactoryBean();
        factory.setValidationMessageSource(messageSource);
        factory.setProviderClass(HibernateValidator.class);
        return factory;
    }

}
