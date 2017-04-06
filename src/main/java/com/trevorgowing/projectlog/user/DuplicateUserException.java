package com.trevorgowing.projectlog.user;

class DuplicateUserException extends RuntimeException {

    private static final long serialVersionUID = -9209842578644208087L;

    static final String REASON = "Duplicate user found with email address";

    DuplicateUserException(String email) {
        super(REASON + ": " + email);
    }
}
