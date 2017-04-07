package com.trevorgowing.projectlog.user;

import com.trevorgowing.projectlog.common.builders.DomainObjectBuilder;

public final class UserNotFoundExceptionBuilder implements DomainObjectBuilder<UserNotFoundException> {

    private long userId;

    public static UserNotFoundExceptionBuilder aUserNotFoundException() {
        return new UserNotFoundExceptionBuilder();
    }

    public UserNotFoundException build() {
        return new UserNotFoundException(userId);
    }

    public UserNotFoundExceptionBuilder id(long userId) {
        this.userId = userId;
        return this;
    }
}
