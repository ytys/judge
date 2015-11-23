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

import java.util.Enumeration;
import java.util.Iterator;
import java.util.Objects;

/**
 *
 * @author zhanhb
 * @param <T> the type of elements returned by this iterator
 */
public class EnumerationIterator<T> implements Iterator<T> {

    public static <T> Iterator<T> of(Enumeration<T> e) {
        return new EnumerationIterator<>(e);
    }

    private final Enumeration<T> e;

    private EnumerationIterator(Enumeration<T> e) {
        this.e = Objects.requireNonNull(e, "enumeration");
    }

    @Override
    public boolean hasNext() {
        return e.hasMoreElements();
    }

    @Override
    public T next() {
        return e.nextElement();
    }

}
