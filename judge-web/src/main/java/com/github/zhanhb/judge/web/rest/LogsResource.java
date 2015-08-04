package com.github.zhanhb.judge.web.rest;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.util.ContextSelectorStaticBinder;
import com.github.zhanhb.judge.web.rest.dto.LoggerDTO;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_XML_VALUE;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for view and managing Log Level at runtime.
 */
@RestController
@RequestMapping("/api")
public class LogsResource {

    @RequestMapping(value = "/logs",
            method = RequestMethod.GET,
            produces = {APPLICATION_JSON_VALUE, APPLICATION_XML_VALUE})
    public List<LoggerDTO> getList() {
        return getLoggerContext()
                .getLoggerList()
                .stream()
                .map(LoggerDTO::new)
                .collect(Collectors.toList());
    }

    @RequestMapping(value = "/logs",
            method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changeLevel(@RequestBody LoggerDTO jsonLogger) {
        getLoggerContext().getLogger(jsonLogger.getName()).setLevel(Level.valueOf(jsonLogger.getLevel()));
    }

    private LoggerContext getLoggerContext() {
        return ContextSelectorStaticBinder
                .getSingleton()
                .getContextSelector()
                .getLoggerContext();
    }

}
