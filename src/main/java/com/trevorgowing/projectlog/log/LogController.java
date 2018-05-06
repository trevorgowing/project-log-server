package com.trevorgowing.projectlog.log;

import static com.trevorgowing.projectlog.log.LogTypeParsingException.causedLogTypeParsingException;
import static com.trevorgowing.projectlog.log.constant.LogConstants.LOGS_URL_PATH;
import static com.trevorgowing.projectlog.log.constant.LogConstants.LOG_ID_URL_PATH;
import static com.trevorgowing.projectlog.log.constant.LogConstants.TYPE_QUERY_PARAMETER;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

import com.trevorgowing.projectlog.common.exception.ExceptionResponse;
import com.trevorgowing.projectlog.log.constant.LogType;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(LOGS_URL_PATH)
class LogController {

  private final LogDeleter logDeleter;
  private final LogRetrieverFactory logRetrieverFactory;

  @GetMapping(produces = APPLICATION_JSON_UTF8_VALUE)
  @ResponseStatus(OK)
  List<LogDTO> getLogs() {
    return logRetrieverFactory.getLogLookupService().getLogDTOs();
  }

  @GetMapping(params = TYPE_QUERY_PARAMETER, produces = APPLICATION_JSON_UTF8_VALUE)
  @ResponseStatus(OK)
  List<LogDTO> getLogs(@RequestParam(name = TYPE_QUERY_PARAMETER) String type) {
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

  @GetMapping(path = LOG_ID_URL_PATH, produces = APPLICATION_JSON_UTF8_VALUE)
  @ResponseStatus(OK)
  LogDTO getLogById(@PathVariable long logId) {
    return logRetrieverFactory.getLogLookupService().getLogDTOById(logId);
  }

  @DeleteMapping(path = LOG_ID_URL_PATH)
  @ResponseStatus(NO_CONTENT)
  void deleteLogById(@PathVariable long logId) {
    logDeleter.deleteLogById(logId);
  }

  @ExceptionHandler(LogTypeParsingException.class)
  public ResponseEntity<ExceptionResponse> handleLogTypeParsingException(
      LogTypeParsingException logTypeParsingException) {
    log.debug(logTypeParsingException.getMessage(), logTypeParsingException);
    return ResponseEntity.status(BAD_REQUEST)
        .contentType(MediaType.APPLICATION_JSON_UTF8)
        .body(ExceptionResponse.from(BAD_REQUEST, logTypeParsingException.getMessage()));
  }

  @ExceptionHandler(LogNotFoundException.class)
  public ResponseEntity<ExceptionResponse> handleLogNotFoundException(
      LogNotFoundException logNotFoundException) {
    log.debug(logNotFoundException.getMessage(), logNotFoundException);
    return ResponseEntity.status(NOT_FOUND)
        .contentType(MediaType.APPLICATION_JSON_UTF8)
        .body(ExceptionResponse.from(NOT_FOUND, logNotFoundException.getMessage()));
  }
}
