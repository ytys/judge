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
public class LoggerRepository {

    /**
     * sort by effective level, real level of current logger, getLevel may
     * return null when inherited from parent level.
     */
    private static final Comparator<Logger> BY_LEVEL = Comparator.comparingInt(x -> x.getEffectiveLevel().toInt());
    /**
     * sort by name, same as the default when no uppercase package declared.
     */
    private static final Comparator<Logger> BY_NAME = Comparator.comparing(x -> x.getName());
    /**
     * sort by name ignoring case, as the order in the dictionary.
     */
    private static final Comparator<Logger> BY_NAME_I = (a, b) -> a.getName().compareToIgnoreCase(b.getName());
    /**
     * sort algorithm in java is stable, we just return 0
     */
    private static final Comparator<Logger> ORIGN_ORDER = (a, b) -> 0;

    static {
        // ensure slf4j is initlized.
        try {
            Class.forName("org.slf4j.impl.StaticLoggerBinder");
        } catch (ClassNotFoundException ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }

    public Optional<Logger> findOne(String name) {
        return Optional.ofNullable(getLoggerContext().exists(name));
    }

    public Iterable<Logger> findAll() {
        return getLoggerContext().getLoggerList();
    }

    public Page<Logger> findAll(Pageable pageable) {
        Objects.requireNonNull(pageable, "pageable");
        List<Logger> all = getLoggerContext().getLoggerList();
        return new PageImpl<>(paging(all, pageable), pageable, all.size());
    }

    public void save(String name, String level) {
        Objects.requireNonNull(name, "name");
        Logger logger = getLoggerContext().exists(name);
        if (logger != null) {
            logger.setLevel(Level.toLevel(level, null));
        }
    }

    private List<Logger> paging(List<Logger> list, Pageable pageable) {
        return list.stream()
                .sorted(
                        Optional.ofNullable(pageable.getSort())
                        .map(this::toComparator)
                        .orElse(ORIGN_ORDER)
                )
                .skip(pageable.getOffset())
                .limit(pageable.getPageSize())
                .collect(Collectors.toList());
    }

    private Comparator<? super Logger> toComparator(Sort sort) {
        Comparator<Logger> result = null;
        for (Sort.Order order : sort) {
            Comparator<Logger> c = "level".equalsIgnoreCase(order.getProperty())
                    ? BY_LEVEL
                    : order.isIgnoreCase() ? BY_NAME_I : BY_NAME;
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