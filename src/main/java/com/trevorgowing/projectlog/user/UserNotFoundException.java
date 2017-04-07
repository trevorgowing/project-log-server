package com.trevorgowing.projectlog.user;

class UserNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 6916075569670660878L;

    static final String REASON = "User not found for id";

    private UserNotFoundException(long userId) {
        super(REASON + ": " + userId);
    }

    static UserNotFoundException identifiedUserNotFoundException(long userId) {
        return new UserNotFoundException(userId);
    }
}
