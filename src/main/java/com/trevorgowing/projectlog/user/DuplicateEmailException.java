package com.trevorgowing.projectlog.user;

class DuplicateEmailException extends RuntimeException {

    private static final long serialVersionUID = -9209842578644208087L;

    static final String REASON = "Duplicate user found with email address";

    private DuplicateEmailException(String email, Throwable cause) {
        super(REASON + ": " + email, cause);
    }

    static DuplicateEmailException causedDuplicateEmailException(String email, Throwable cause) {
        return new DuplicateEmailException(email, cause);
    }
}
