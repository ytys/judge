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

import java.io.Serializable;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.Repository;
import org.springframework.data.rest.core.annotation.RestResource;

/**
 *
 * Our base repository rather extends {@link PagingAndSortingRepository}
 *
 * @author zhanhb
 * @param <T> Entity class
 * @param <ID> id class of the entity, usually java.lang.Long
 * @see PagingAndSortingRepository
 */
@NoRepositoryBean
public interface BaseRepository<T, ID extends Serializable> extends Repository<T, ID>, QueryDslPredicateExecutor<T> {

    @RestResource(exported = false)
    <S extends T> S save(S entity);

    @RestResource(exported = false)
    <S extends T> Iterable<S> save(Iterable<S> entities);

    Optional<T> findOne(ID id);

    boolean exists(ID id);

    @RestResource(exported = false)
    Iterable<T> findAll();

    // Iterable<T> findAll(Iterable<ID> ids);
    long count();

    @RestResource(exported = false)
    void delete(ID id);

    @RestResource(exported = false)
    void delete(T entity);

    @RestResource(exported = false)
    void delete(Iterable<? extends T> entities);

    @RestResource(exported = false)
    void deleteAll();

    @RestResource(exported = false)
    Iterable<T> findAll(Sort sort);

    Page<T> findAll(Pageable pageable);

}
