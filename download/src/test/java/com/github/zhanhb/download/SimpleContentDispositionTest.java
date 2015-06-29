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
package com.github.zhanhb.download;

import static net.sourceforge.jwebunit.junit.JWebUnit.assertTitleEquals;
import static net.sourceforge.jwebunit.junit.JWebUnit.beginAt;
import static net.sourceforge.jwebunit.junit.JWebUnit.clickLink;
import static net.sourceforge.jwebunit.junit.JWebUnit.setBaseUrl;
import static net.sourceforge.jwebunit.junit.JWebUnit.setTextField;
import static net.sourceforge.jwebunit.junit.JWebUnit.submit;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author zhanhb
 */
public class SimpleContentDispositionTest {

    @Before
    public void setup() {
        setBaseUrl("http://localhost/download_test");
    }

    @Test
    public void testLogin() {
        beginAt("/index.jsp");
        clickLink("login");
        assertTitleEquals("Login");
        setTextField("username", "test");
        setTextField("password", "test123");
        submit();
        assertTitleEquals("Welcome, test!");
    }
}
