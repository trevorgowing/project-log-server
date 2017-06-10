package com.trevorgowing.projectlog.log;

import com.trevorgowing.projectlog.log.constant.LogConstants;
import com.trevorgowing.projectlog.log.constant.LogType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.trevorgowing.projectlog.log.LogTypeParsingException.causedLogTypeParsingException;

@Slf4j
@RestController
@RequestMapping(LogConstants.LOGS_URL_PATH)
class LogController {

    private final LogCRUDServiceFactory logCRUDServiceFactory;

    LogController(LogCRUDServiceFactory logCRUDServiceFactory) {
        this.logCRUDServiceFactory = logCRUDServiceFactory;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.OK)
    List<LogDTO> getLogs() {
        return logCRUDServiceFactory.getLogCRUDService().getLogDTOs();
    }

    @GetMapping(params = LogConstants.TYPE_QUERY_PARAMETER, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.OK)
    List<LogDTO> getLogs(@RequestParam(name = LogConstants.TYPE_QUERY_PARAMETER) String type) {
        LogType logType;

        try {
            logType = parseLogType(type);
        } catch (IllegalArgumentException | NullPointerException exception) {
            throw causedLogTypeParsingException(type, exception);
        }

        return logCRUDServiceFactory.getLogCRUDService(logType).getLogDTOs();
    }

    private LogType parseLogType(String type) {
        return LogType.valueOf(type.trim().toUpperCase());
    }

    @ExceptionHandler(LogTypeParsingException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = LogTypeParsingException.REASON)
    public void handleLogTypeParsingException(LogTypeParsingException logTypeParsingException) {
        log.warn(logTypeParsingException.getMessage(), logTypeParsingException);
    }
}