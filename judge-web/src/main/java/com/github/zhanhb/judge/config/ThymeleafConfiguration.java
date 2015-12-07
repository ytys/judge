package com.github.zhanhb.judge.config;

import java.util.LinkedHashMap;
import javax.annotation.PostConstruct;
import javax.servlet.Servlet;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.template.TemplateLocation;
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafAutoConfiguration;
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.util.MimeType;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;

@Configuration
public class ThymeleafConfiguration {

    private static final Log logger = LogFactory.getLog(ThymeleafAutoConfiguration.class);

    @Configuration
    @AutoConfigureBefore(ThymeleafAutoConfiguration.DefaultTemplateResolverConfiguration.class)
    public static class DefaultTemplateResolverConfiguration {

        @Autowired
        private ThymeleafProperties properties;

        @Autowired
        private ApplicationContext applicationContext;

        @PostConstruct
        public void checkTemplateLocationExists() {
            boolean checkTemplateLocation = this.properties.isCheckTemplateLocation();
            if (checkTemplateLocation) {
                TemplateLocation location = new TemplateLocation(
                        this.properties.getPrefix());
                if (!location.exists(this.applicationContext)) {
                    logger.warn("Cannot find template location: " + location
                            + " (please add some templates or check "
                            + "your Thymeleaf configuration)");
                }
            }
        }

        @Bean
        public SpringResourceTemplateResolver defaultTemplateResolver() {
            SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();
            resolver.setApplicationContext(applicationContext);
            resolver.setPrefix(this.properties.getPrefix());
            resolver.setSuffix(this.properties.getSuffix());
            resolver.setTemplateMode(this.properties.getMode());
            if (this.properties.getEncoding() != null) {
                resolver.setCharacterEncoding(this.properties.getEncoding().name());
            }
            resolver.setCacheable(this.properties.isCache());
            Integer order = this.properties.getTemplateResolverOrder();
            if (order != null) {
                resolver.setOrder(order);
            }
            return resolver;
        }

    }

    @Configuration
    @ConditionalOnClass({Servlet.class})
    @ConditionalOnWebApplication
    protected static class ThymeleafViewResolverConfiguration {

        @Autowired
        private ThymeleafProperties properties;

        @Autowired
        private SpringTemplateEngine templateEngine;

        @Bean
        @ConditionalOnMissingBean(name = "thymeleafViewResolver")
        @ConditionalOnProperty(name = "spring.thymeleaf.enabled", matchIfMissing = true)
        public ThymeleafViewResolver thymeleafViewResolver() {
            ThymeleafViewResolver resolver = new ThymeleafViewResolver();
            resolver.setTemplateEngine(this.templateEngine);
            resolver.setCharacterEncoding(this.properties.getEncoding().name());
            resolver.setContentType(appendCharset(this.properties.getContentType(),
                    resolver.getCharacterEncoding()));
            resolver.setExcludedViewNames(this.properties.getExcludedViewNames());
            resolver.setViewNames(this.properties.getViewNames());
            // This resolver acts as a fallback resolver (e.g. like a
            // InternalResourceViewResolver) so it needs to have low precedence
            resolver.setOrder(Ordered.LOWEST_PRECEDENCE - 5);
            return resolver;
        }

        private String appendCharset(MimeType type, String charset) {
            if (type.getCharSet() != null) {
                return type.toString();
            }
            LinkedHashMap<String, String> parameters = new LinkedHashMap<String, String>();
            parameters.put("charset", charset);
            parameters.putAll(type.getParameters());
            return new MimeType(type, parameters).toString();
        }

    }

}
