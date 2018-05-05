package com.trevorgowing.projectlog.log;

class LogNotFoundException extends RuntimeException {

  private static final long serialVersionUID = -853551485580322759L;

  static final String REASON = "Log not found for id";

  private LogNotFoundException(long logId) {
    super(REASON + ": " + logId);
  }

  static LogNotFoundException identifiedLogNotFoundException(long logId) {
    return new LogNotFoundException(logId);
  }
}
