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
/**
 * JPA domain objects.
 *
 * @author zhanhb
 */
/* (non-Javadoc)
 *
 * Global definition for hibernate jsr 310 date time type.
 * It seems that liquibase doesn't work with this.
 */
@org.hibernate.annotations.TypeDefs({
    @org.hibernate.annotations.TypeDef(name = "localDateType", typeClass = org.jadira.usertype.dateandtime.threeten.PersistentLocalDate.class, defaultForType = java.time.LocalDate.class),
    @org.hibernate.annotations.TypeDef(name = "localDateTimeType", typeClass = org.jadira.usertype.dateandtime.threeten.PersistentLocalDateTime.class, defaultForType = java.time.LocalDateTime.class),
    @org.hibernate.annotations.TypeDef(name = "localTimeType", typeClass = org.jadira.usertype.dateandtime.threeten.PersistentLocalTime.class, defaultForType = java.time.LocalTime.class)
})
package com.github.zhanhb.judge.domain;
