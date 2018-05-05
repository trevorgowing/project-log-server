package com.trevorgowing.projectlog.log;

import static com.trevorgowing.projectlog.log.LogNotFoundException.identifiedLogNotFoundException;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

@Service
class LogDeleter {

  private final LogRepository logRepository;

  LogDeleter(LogRepository logRepository) {
    this.logRepository = logRepository;
  }

  void deleteLogById(long logId) {
    try {
      logRepository.delete(logId);
    } catch (EmptyResultDataAccessException emptyResultDataAccessException) {
      throw identifiedLogNotFoundException(logId);
    }
  }
}
