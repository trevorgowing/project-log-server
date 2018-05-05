package com.trevorgowing.projectlog.log;

import static com.trevorgowing.projectlog.log.LogNotFoundException.identifiedLogNotFoundException;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class LogDeleter {

  private final LogRepository logRepository;

  void deleteLogById(long logId) {
    try {
      logRepository.deleteById(logId);
    } catch (EmptyResultDataAccessException emptyResultDataAccessException) {
      throw identifiedLogNotFoundException(logId);
    }
  }
}
