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

import com.google.common.base.Strings;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Locale;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.embedded.EmbeddedWebApplicationContext;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;

/**
 * Boot class for both web application and spring-boot application.
 *
 * @author zhanhb
 */
@Slf4j
@SpringBootApplication
public class Application extends SpringBootServletInitializer {

    public static void main(String[] args) throws IOException {
        ApplicationContext ctx = start(args);
        try (InputStreamReader isr = new InputStreamReader(System.in);
                BufferedReader br = new BufferedReader(isr)) {
            for (;;) {
                String line = br.readLine();
                if (line == null) {
                    break;
                }
                switch (line.toLowerCase(Locale.US)) {
                    case "exit":
                    case "quit":
                        SpringApplication.exit(ctx);
                        // break will only break the switch block.
                        return;
                    case "break":
                        return;
                    case "reload":
                        SpringApplication.exit(ctx);
                        ctx = start(args);
                        continue;
                }
                String property = ctx.getEnvironment().getProperty(line);
                log.info(property);
            }
        }
    }

    private static ApplicationContext start(String[] args) {
        ApplicationContext context = SpringApplication.run(Application.class, args);
        if (log.isInfoEnabled() && context instanceof EmbeddedWebApplicationContext) {
            int port = ((EmbeddedWebApplicationContext) context).getEmbeddedServletContainer().getPort();
            String contextPath = context.getApplicationName();
            String url = String.format(Locale.US, "http://localhost:%d%s", port, contextPath);
            String dashes = Strings.repeat("-", 72);
            log.info("Access URLs:\n{}\n\tLocal: \t\t{}\n{}", dashes, url, dashes);
        }
        return context;
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.bannerMode(Banner.Mode.OFF)
                .addCommandLineProperties(false)
                .logStartupInfo(true)
                .sources(Application.class);
    }

}
