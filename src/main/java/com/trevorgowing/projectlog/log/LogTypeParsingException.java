package com.trevorgowing.projectlog.log;

class LogTypeParsingException extends RuntimeException {

    private static final long serialVersionUID = -3099448268280334982L;

    static final String REASON = "Unable to parse LogType, expected \"RISK\" or \"ISSUE\"";

    private LogTypeParsingException(String type, Throwable cause) {
        super(REASON + " but found: " + type, cause);
    }

    static LogTypeParsingException causedLogTypeParsingException(String type, Throwable cause) {
        return new LogTypeParsingException(type, cause);
    }
}