package com.github.zhanhb.judge.web.rest;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.util.ContextSelectorStaticBinder;
import com.github.zhanhb.judge.web.rest.dto.LoggerDTO;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
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

    private static final Comparator<Logger> BY_NAME = Comparator.comparing(x -> x.getName());
    private static final Comparator<Logger> BY_LEVEL = Comparator.comparingInt(x -> x.getEffectiveLevel().toInt());
    private static final Comparator<Logger> BY_NAME_I = (a, b) -> a.getName().compareToIgnoreCase(b.getName());

    @RequestMapping(value = "/logs", method = RequestMethod.GET,
            produces = {APPLICATION_JSON_VALUE, APPLICATION_XML_VALUE})
    public PagedResources<Resource<LoggerDTO>> getList(
            PagedResourcesAssembler<LoggerDTO> assembler, Pageable pageable) {
        List<Logger> all = getLoggerContext().getLoggerList();
        List<LoggerDTO> filtered = filter(all, pageable);
        return assembler.toResource(new PageImpl<>(filtered, pageable, all.size()));
    }

    @RequestMapping(value = "/logs", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changeLevel(@RequestBody LoggerDTO jsonLogger) {
        Logger logger = getLoggerContext().exists(jsonLogger.getName());
        if (logger != null) {
            logger.setLevel(Level.toLevel(jsonLogger.getLevel(), null));
        }
    }

    private LoggerContext getLoggerContext() {
        return ContextSelectorStaticBinder
                .getSingleton()
                .getContextSelector()
                .getLoggerContext();
    }

    private List<LoggerDTO> filter(List<Logger> list, Pageable pageable) {
        return list.stream()
                .sorted(toComparator(pageable.getSort()))
                .skip(pageable.getOffset())
                .limit(pageable.getPageSize())
                .map(LoggerDTO::new)
                .collect(Collectors.toList());
    }

    private Comparator<? super Logger> toComparator(Sort sort) {
        if (sort == null) {
            return BY_NAME;
        }
        Comparator<Logger> result = null;
        for (Sort.Order order : sort) {
            Comparator<Logger> c = "level".equalsIgnoreCase(order.getProperty())
                    ? BY_LEVEL
                    : order.isIgnoreCase() ? BY_NAME_I : BY_NAME;
            c = order.isAscending() ? c : c.reversed();
            result = result == null ? c : result.thenComparing(c);
        }
        return result == null ? BY_NAME : result.thenComparing(BY_NAME);
    }

}
