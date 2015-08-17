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
package com.github.zhanhb.judge.repository;

import com.github.zhanhb.judge.domain.QUserprofile;
import com.github.zhanhb.judge.domain.Userprofile;
import java.util.Optional;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

/**
 *
 * @author zhanhb
 */
public interface UserprofileRepository extends BaseRepository<Userprofile, Long>, QueryDslPredicateExecutor<Userprofile>, QuerydslBinderCustomizer<QUserprofile> {

    @RestResource(path = "by-handle")
    Optional<Userprofile> findByHandleIgnoreCase(@Param("handle") String handle);

    @RestResource(path = "by-email")
    Optional<Userprofile> findByEmail(@Param("email") String email);

    @Override
    default void customize(QuerydslBindings bindings, QUserprofile userprofile) {
        bindings.excluding(userprofile.password);
    }

}
