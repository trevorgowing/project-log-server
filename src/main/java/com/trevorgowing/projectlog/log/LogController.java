package com.trevorgowing.projectlog.log;

import com.trevorgowing.projectlog.log.constant.LogConstants;
import com.trevorgowing.projectlog.log.constant.LogType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.trevorgowing.projectlog.log.LogTypeParsingException.causedLogTypeParsingException;

@Slf4j
@RestController
@RequestMapping(LogConstants.LOGS_URL_PATH)
class LogController {

    private final LogDeleter logDeleter;
    private final LogRetrieverFactory logRetrieverFactory;

    LogController(LogDeleter logDeleter, LogRetrieverFactory logRetrieverFactory) {
        this.logDeleter = logDeleter;
        this.logRetrieverFactory = logRetrieverFactory;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.OK)
    List<LogDTO> getLogs() {
        return logRetrieverFactory.getLogLookupService().getLogDTOs();
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

        return logRetrieverFactory.getLogLookupService(logType).getLogDTOs();
    }

    private LogType parseLogType(String type) {
        return LogType.valueOf(type.trim().toUpperCase());
    }

    @GetMapping(path = LogConstants.LOG_ID_URL_PATH, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.OK)
    LogDTO getLogById(@PathVariable long logId) {
        return logRetrieverFactory.getLogLookupService().getLogDTOById(logId);
    }

    @DeleteMapping(path = LogConstants.LOG_ID_URL_PATH)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteLogById(@PathVariable long logId) {
        logDeleter.deleteLogById(logId);
    }

    @ExceptionHandler(LogTypeParsingException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = LogTypeParsingException.REASON)
    public void handleLogTypeParsingException(LogTypeParsingException logTypeParsingException) {
        log.warn(logTypeParsingException.getMessage(), logTypeParsingException);
    }

    @ExceptionHandler(LogNotFoundException.class)
    @ResponseStatus(code = HttpStatus.NOT_FOUND, reason = LogNotFoundException.REASON)
    public void handleLogNotFoundException(LogNotFoundException logNotFoundException) {
        log.warn(logNotFoundException.getMessage(), logNotFoundException);
    }
}