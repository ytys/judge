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
package com.github.zhanhb.judge.util;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 *
 * @author zhanhb
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
    CustomInvocationHandlerTest.class,
    MatcherWrapperTest.class,
    PasswordEncoderTest.class,
    StringsTest.class,
    UriComponentsBuilderTest.class,
    ZipTest.class
})
public class TestSuite {

}
