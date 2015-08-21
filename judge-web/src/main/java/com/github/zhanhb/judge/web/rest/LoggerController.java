package com.github.zhanhb.judge.web.rest;

import com.github.zhanhb.judge.repository.LoggerRepository;
import com.github.zhanhb.judge.web.rest.dto.LoggerDTO;
import java.util.Objects;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.ExposesResourceFor;
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
 *
 * @author zhanhb
 */
@ExposesResourceFor(LoggerDTO.class)
@RequestMapping("api/logs")
@RestController
public class LoggerController {

    private final LoggerRepository loggers;

    @Autowired
    public LoggerController(LoggerRepository loggers) {
        this.loggers = Objects.requireNonNull(loggers);
    }

    @RequestMapping(method = RequestMethod.GET,
            produces = {APPLICATION_JSON_VALUE, APPLICATION_XML_VALUE})
    public PagedResources<Resource<LoggerDTO>> list(
            PagedResourcesAssembler<LoggerDTO> assembler, Pageable pageable) {
        return assembler.toResource(loggers.findAll(pageable).map(LoggerDTO::new));
    }

    @RequestMapping(method = RequestMethod.POST,
            consumes = {APPLICATION_JSON_VALUE, APPLICATION_XML_VALUE})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changeLevel(@Valid @RequestBody LoggerDTO json) {
        loggers.save(json.getName(), json.getLevel());
    }

}
