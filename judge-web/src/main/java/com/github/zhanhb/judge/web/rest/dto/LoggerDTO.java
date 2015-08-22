package com.github.zhanhb.judge.web.rest.dto;

import ch.qos.logback.classic.Logger;
import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * Logger data transfer object.
 *
 * @author zhanhb
 */
@Data
@NoArgsConstructor
public class LoggerDTO {

    @NotNull
    private String name;
    @Nullable
    private String level;

    public LoggerDTO(@NonNull Logger logger) {
        this.name = logger.getName();
        this.level = logger.getEffectiveLevel().toString();
    }

}
