package com.trevorgowing.projectlog.log;

import com.trevorgowing.projectlog.log.constant.LogConstants;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(LogConstants.LOGS_URL_PATH)
class LogController {

    private final LogCRUDService logCRUDService;

    LogController(LogCRUDService logCRUDService) {
        this.logCRUDService = logCRUDService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.OK)
    List<LogDTO> getLogs() {
        return logCRUDService.getLogDTOs();
    }
}