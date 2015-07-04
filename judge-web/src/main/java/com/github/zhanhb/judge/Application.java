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
import java.net.InetAddress;
import java.util.Scanner;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 *
 * @author zhanhb
 */
@Slf4j
@SpringBootApplication
public class Application {

    public static void main(String[] args) throws IOException {
        ConfigurableApplicationContext ctx = SpringApplication.run(Application.class, args);
        String port = ctx.getEnvironment().getProperty("server.port");

        log.info("Access URLs:\n----------------------------------------------------------\n\t"
                + "Local: \t\thttp://127.0.0.1:{}\n\t"
                + "External: \thttp://{}:{}\n----------------------------------------------------------",
                port,
                InetAddress.getLocalHost().getHostAddress(),
                port);

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String name = scanner.next();
            switch (name) {
                case "exit":
                case "quit":
                    SpringApplication.exit(ctx);
                    return;
                case "reload":
                    SpringApplication.exit(ctx);
                    ctx = SpringApplication.run(Application.class, args);
                    continue;
            }
            String property = ctx.getEnvironment().getProperty(name);
            System.out.println(property);
        }
    }

}
