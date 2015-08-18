package com.github.zhanhb.judge.web.rest.dto;

import ch.qos.logback.classic.Logger;
import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.NonNull;

@Data
public class LoggerDTO {

    @NotNull
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
