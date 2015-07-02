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
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.junit.Test;

/**
 *
 * @author zhanhb
 * @date Jun 5, 2015, 14:11:28
 */
@Slf4j
public class PagesTest {

    private static final Set<String> collect;

    static {
        collect = Collections.unmodifiableSet(
                Arrays.asList("a,abbr,address,area,article,aside,audio,b,base,bb,bdo,blockquote,body,br,button,canvas,caption,cite,code,col,colgroup,command,datagrid,datalist,dd,del,details,dfn,dialog,div,dl,dt,em,embed,fieldset,figure,footer,form,h1,h2,h3,h4,h5,h6,head,header,hr,html,i,iframe,img,input,ins,kbd,label,legend,li,link,map,mark,menu,meta,meter,nav,noscript,object,ol,optgroup,option,output,p,param,pre,progress,q,rp,rt,ruby,samp,script,section,select,small,source,span,strong,style,sub,sup,table,tbody,td,textarea,tfoot,th,thead,time,title,tr,ul,var,video".split(","))
                .stream().collect(Collectors.toSet()));
    }

    private Path current;
    private final Set<String> namespaces = new HashSet<>(10);
    private Element root;
    private Document document;

    @Test
    public void test() throws IOException {
        log.info("{0}", collect);
        Predicate<String> pattern = Pattern.compile("(?i)\\.(?:jsp|tag)x").asPredicate();
        Files.walk(Paths.get("src"))
                .filter(path -> pattern.test(path.toString()))
                .forEach(this::check);

        System.out.println(namespaces);
    }

    private void check(Path path) {
        try {
            current = path;
            check0(path);
        } catch (IOException | DocumentException ex) {
            throw new RuntimeException(path.toString() + " " + ex, ex);
        }
    }

    private void check0(Path path) throws IOException, DocumentException {
        SAXReader reader = new SAXReader();
        try (InputStream is = Files.newInputStream(path)) {
            document = reader.read(is);
        }
        root = document.getRootElement();
        checkRoot(root);
        visit(root);
    }

    private void visit(Element rootElement) {
        checkElement(rootElement);
        List<?> elements = rootElement.elements();
        elements.stream().filter(e -> (e instanceof Element)).forEach((e) -> {
            visit((Element) e);
        });
    }

    private void checkElement(Element element) {
        checkTagName(element);
        checkDoctypeSystem(element);
    }

    private void checkTagName(Element element) {
        String namespace = element.getNamespace().getText();
        namespaces.add(namespace);
        if (namespace.isEmpty() || "http://www.w3.org/1999/xhtml".equals(namespace)) {
            String name = element.getName();
            if (!name.isEmpty()) {
                if (!collect.contains(element.getName())) {
                    throw new IllegalArgumentException(element.getName() + " " + current);
                }
            }
        }
    }

    private void checkDoctypeSystem(Element element) {
        Attribute attribute = element.attribute("doctype-system");
        if (attribute != null) {
            if (!root.getName().equals("html")) {
                throw new IllegalArgumentException("root '" + root.getName() + "' has an element with attribute doctype-system=\"" + attribute.getValue() + "\"");
            }
        }
    }

    private void checkRoot(Element root) {
        if (!root.getName().equals("html") && !root.getNamespaceForPrefix("").getURI().isEmpty()) {
            throw new IllegalArgumentException(current + " " + pretty(root));
        }
    }

    private String pretty(Element element) {
        return '<' + element.getNamespacePrefix() + ":" + element.getName() + " xmlns=\"" + root.getNamespaceForPrefix("").getURI() + "\">";
    }
}
