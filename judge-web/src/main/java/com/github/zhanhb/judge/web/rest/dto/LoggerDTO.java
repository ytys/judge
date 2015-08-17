package com.github.zhanhb.judge.web.rest.dto;

import ch.qos.logback.classic.Logger;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import lombok.Data;
import lombok.NonNull;

@Data
public class LoggerDTO {

    @Nonnull
    private String name;
    @Nullable
    private String level;

    // for accessor of json creator
    public LoggerDTO() {
    }

    public LoggerDTO(@NonNull Logger logger) {
        this.name = logger.getName();
        this.level = logger.getEffectiveLevel().toString();
    }

}
