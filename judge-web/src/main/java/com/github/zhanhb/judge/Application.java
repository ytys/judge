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
package com.github.zhanhb.judge;

import java.io.IOException;
import java.util.Locale;
import java.util.Scanner;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.embedded.EmbeddedWebApplicationContext;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

/**
 *
 * @author zhanhb
 */
@Slf4j
@SpringBootApplication
public class Application extends SpringBootServletInitializer {

    public static void main(String[] args) throws IOException {
        ApplicationContext ctx = start(null, args);
        try (Scanner scanner = new Scanner(System.in)) {
            while (scanner.hasNext()) {
                String name = scanner.next();
                switch (name) {
                    case "exit":
                    case "quit":
                        SpringApplication.exit(ctx);
                        return;
                    case "break":
                        return;
                    case "reload":
                        ctx = start(ctx, args);
                        continue;
                }
                String property = ctx.getEnvironment().getProperty(name);
                System.out.println(property);
            }
        }
    }

    private static ApplicationContext start(ApplicationContext old, String[] args) {
        if (old != null) {
            SpringApplication.exit(old);
        }
        String dashes = "------------------------------------------------------------------------";
        ConfigurableApplicationContext ctx = SpringApplication.run(Application.class, args);
        if (log.isInfoEnabled() && ctx instanceof EmbeddedWebApplicationContext) {
            EmbeddedWebApplicationContext context = (EmbeddedWebApplicationContext) ctx;
            int port = context.getEmbeddedServletContainer().getPort();
            String contextPath = context.getApplicationName();
            String url = String.format(Locale.US, "http://localhost:%d%s", port, contextPath);
            log.info("Access URLs:\n{}\n\tLocal: \t\t{}\n{}", dashes, url, dashes);
        }
        return ctx;
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.showBanner(false)
                .addCommandLineProperties(false)
                .logStartupInfo(true)
                .sources(Application.class);
    }

}
