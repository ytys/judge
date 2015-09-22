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

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.util.ContextSelectorStaticBinder;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

/**
 *
 * @author zhanhb
 */
@Service
// ensure slf4j is initlized.
@Slf4j
public class LoggerRepository {

    /**
     * sort by effective level, real level of current logger, getLevel may
     * return null when inherited from parent logger.
     */
    private static final Comparator<Logger> BY_LEVEL = Comparator.comparingInt(x -> x.getEffectiveLevel().toInt());

    /**
     * sort by name, same as the default when no uppercase package declared.
     */
    private static final Comparator<Logger> BY_NAME = Comparator.comparing(Logger::getName);

    /**
     * sort by name ignoring case, as the order in the dictionary.
     */
    private static final Comparator<Logger> BY_NAME_I = Comparator.comparing(Logger::getName, String.CASE_INSENSITIVE_ORDER);

    public Optional<Logger> findOne(String name) {
        return Optional.ofNullable(getLoggerContext().exists(Objects.requireNonNull(name, "name")));
    }

    public Iterable<Logger> findAll() {
        return getLoggerContext().getLoggerList();
    }

    public Page<Logger> findAll(Pageable pageable) {
        Objects.requireNonNull(pageable, "pageable");
        List<Logger> all = getLoggerContext().getLoggerList();
        return new PageImpl<>(paging(all.stream(), pageable), pageable, all.size());
    }

    public void save(String name, String level) {
        findOne(name).ifPresent(logger -> logger.setLevel(Level.toLevel(level, null)));
    }

    private List<Logger> paging(Stream<Logger> stream, Pageable pageable) {
        return Optional.ofNullable(pageable.getSort()) // maybe api returns null
                .map(this::toComparator) // usually not null, throws an IAE if the property is not present
                .map(stream::sorted)
                .orElse(stream)
                .skip(pageable.getOffset())
                .limit(pageable.getPageSize())
                .collect(Collectors.toList());
    }

    private Comparator<? super Logger> toComparator(Sort sort) {
        Comparator<Logger> result = null;
        for (Sort.Order order : sort) {
            String property = order.getProperty();

            Comparator<Logger> c;
            if ("level".equalsIgnoreCase(property)) {
                c = BY_LEVEL;
            } else if ("name".equalsIgnoreCase(property)) {
                c = order.isIgnoreCase() ? BY_NAME_I : BY_NAME;
            } else {
                throw new IllegalArgumentException(String.format("No property %s found for type %s!", property, Logger.class.getSimpleName()));
            }
            c = order.isAscending() ? c : c.reversed();
            result = result == null ? c : result.thenComparing(c);
        }
        return result;
    }

    private LoggerContext getLoggerContext() {
        return ContextSelectorStaticBinder
                .getSingleton()
                .getContextSelector()
                .getLoggerContext();
    }

}
