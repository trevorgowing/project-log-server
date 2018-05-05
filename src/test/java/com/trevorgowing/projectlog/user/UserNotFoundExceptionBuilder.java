package com.trevorgowing.projectlog.user;

import static com.trevorgowing.projectlog.user.UserNotFoundException.identifiedUserNotFoundException;

import com.trevorgowing.projectlog.common.builders.DomainObjectBuilder;

public final class UserNotFoundExceptionBuilder
    implements DomainObjectBuilder<UserNotFoundException> {

  private long userId;

  public static UserNotFoundExceptionBuilder aUserNotFoundException() {
    return new UserNotFoundExceptionBuilder();
  }

  public UserNotFoundException build() {
    return identifiedUserNotFoundException(userId);
  }

  public UserNotFoundExceptionBuilder id(long userId) {
    this.userId = userId;
    return this;
  }
}
