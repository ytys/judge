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
package com.github.zhanhb.judge.web.form;

/**
 *
 * @author zhanhb
 */
interface Constants {

    @SuppressWarnings({"PublicInnerClass", "UtilityClassWithoutPrivateConstructor"})
    abstract class Patterns {

        private static final String userNameField = "[a-z0-9!#$%&'*+/=?^_`{|}~-]";
        private static final String lowerAlpha = "[a-z0-9]";
        private static final String lowerAlphaDash = "[a-z0-9-]";
        private static final String domainField = lowerAlpha + "(?:" + lowerAlphaDash + "*" + lowerAlpha + ")?+";

        public static final String EMAIL = userNameField + "++(?:\\." + userNameField + "++)*+@"
                + domainField + "(?:\\." + domainField + ")++";

        public static final String EMAIL_OR_EMPRTY = "(?:" + EMAIL + ")?";

    }

}
