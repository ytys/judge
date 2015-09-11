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
package com.github.zhanhb.judge.main;

import com.github.zhanhb.judge.util.EnumerationIterator;
import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Optional;
import java.util.function.Consumer;

/**
 *
 * @author zhanhb
 * @date May 31, 2015, 23:23:44
 */
public class Finder {

    public static void main(String[] args) throws Throwable {
        Consumer<URL> c = System.out::println;
        consume(javax.xml.stream.EventFilter.class, c);
        consume(javax.annotation.Generated.class, c);
        consume(javax.annotation.CheckForNull.class, c);
        consume(aj.org.objectweb.asm.ClassVisitor.class, c);
        //consume(com.sun.xml.internal.ws.org.objectweb.asm.ClassVisitor.class, c);
        consume(org.springframework.asm.ClassVisitor.class, c);
        //consume(jdk.internal.org.objectweb.asm.ClassVisitor.class, c);
        consume(org.mockito.asm.ClassVisitor.class, c);

        consume(org.mockito.cglib.util.StringSwitcher.class, c);
        consume(org.springframework.cglib.util.StringSwitcher.class, c);
    }

    public static void consume(Class<?> cl, Consumer<URL> consumer) throws IOException {
        Enumeration<URL> e = Optional.ofNullable(cl.getClassLoader())
                .orElseGet(Thread.currentThread()::getContextClassLoader)
                .getResources(cl.getName().replace('.', '/').concat(".class"));
        EnumerationIterator.of(e).forEachRemaining(consumer);
    }
}
