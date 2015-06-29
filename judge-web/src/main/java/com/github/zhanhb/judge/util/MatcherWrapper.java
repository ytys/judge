package com.github.zhanhb.judge.util;

import static java.util.Objects.requireNonNull;
import java.util.function.Function;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("FinalClass")
public final class MatcherWrapper implements MatchResult {

    public static String quoteReplacement(String s) {
        return Matcher.quoteReplacement(s);
    }

    private final Matcher matcher;
    private final CharSequence text;
    private boolean locked;

    public MatcherWrapper(Pattern pattern, CharSequence input) {
        this.matcher = pattern.matcher(input);
        this.text = input;
    }

    public String replaceAll(String replacement) {
        checkLocked();
        return matcher.replaceAll(replacement);
    }

    public String replaceAll(Function<MatcherWrapper, String> replaceFunction) {
        checkLocked();
        requireNonNull(replaceFunction);
        Matcher m = matcher.reset();
        boolean result = m.find();
        if (result) {
            @SuppressWarnings("StringBufferWithoutInitialCapacity")
            StringBuffer sb = new StringBuffer();
            do {
                String replacement;
                try {
                    lock();
                    replacement = requireNonNull(replaceFunction.apply(this));
                } finally {
                    unlock();
                }
                result = m.appendReplacement(sb, Matcher.quoteReplacement(replacement)).find();
            } while (result);
            return m.appendTail(sb).toString();
        }
        return text.toString();
    }

    public String replaceFirst(Function<MatcherWrapper, String> replaceFunction) {
        requireNonNull(replaceFunction, "replaceFunction");
        checkLocked();
        Matcher m = matcher;
        m.reset();
        if (!m.find()) {
            return text.toString();
        }
        String replacement;
        try {
            lock();
            replacement = requireNonNull(replaceFunction.apply(this));
        } finally {
            unlock();
        }
        @SuppressWarnings("StringBufferWithoutInitialCapacity")
        StringBuffer sb = new StringBuffer();
        return m.appendReplacement(sb, Matcher.quoteReplacement(replacement)).appendTail(sb).toString();
    }

    public String replaceFirst(String replacement) {
        checkLocked();
        return matcher.replaceFirst(replacement);
    }

    public Pattern pattern() {
        return matcher.pattern();
    }

    public MatcherWrapper reset() {
        checkLocked();
        matcher.reset();
        return this;
    }

    @Override
    public int start() {
        return matcher.start();
    }

    @Override
    public int start(int group) {
        return matcher.start(group);
    }

    public int start(String name) {
        return matcher.start(name);
    }

    @Override
    public int end() {
        return matcher.end();
    }

    @Override
    public int end(int group) {
        return matcher.end(group);
    }

    public int end(String name) {
        return matcher.end(name);
    }

    @Override
    public String group() {
        return matcher.group();
    }

    @Override
    public String group(int group) {
        return matcher.group(group);
    }

    public String group(String name) {
        return matcher.group(name);
    }

    @Override
    public int groupCount() {
        return matcher.groupCount();
    }

    public boolean matches() {
        checkLocked();
        return matcher.matches();
    }

    public boolean find() {
        checkLocked();
        return matcher.find();
    }

    public boolean find(int start) {
        checkLocked();
        return matcher.find(start);
    }

    public MatcherWrapper appendReplacement(StringBuffer sb, String replacement) {
        checkLocked();
        matcher.appendReplacement(sb, replacement);
        return this;
    }

    public StringBuffer appendTail(StringBuffer sb) {
        checkLocked();
        return matcher.appendTail(sb);
    }

    private void lock() {
        locked = true;
    }

    private void unlock() {
        locked = false;
    }

    private void checkLocked() {
        if (locked) {
            throw new IllegalStateException("Matcher Locked");
        }
    }
}
