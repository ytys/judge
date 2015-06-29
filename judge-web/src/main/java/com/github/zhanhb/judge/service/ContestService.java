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
package com.github.zhanhb.judge.service;

import com.github.zhanhb.judge.repository.ContestRepository;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.util.concurrent.UncheckedExecutionException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContestService {

    @Autowired
    private ContestRepository repository;

    private final LoadingCache<String, Boolean> cache = CacheBuilder
            .newBuilder()
            .maximumSize(200)
            .expireAfterAccess(1, TimeUnit.MINUTES)
            .initialCapacity(20).build(new CacheLoader<String, Boolean>() {

                @Override
                public Boolean load(String path) {
                    return repository.findByNameIgnoreCase(path).isPresent();
                }
            });

    public boolean shouldGotoContest(String servletPath) {
        if (servletPath.charAt(0) != '/') {
            return false;
        }
        int indexOf = servletPath.indexOf('/', 1);
        if (indexOf == -1) {
            indexOf = servletPath.length();
        }
        String path = servletPath.substring(1, indexOf);
        if (path.length() == 0) {
            return false;
        }
        try {
            return cache.get(path);
        } catch (ExecutionException ex) {
            throw new UncheckedExecutionException(ex);
        }
    }

}
