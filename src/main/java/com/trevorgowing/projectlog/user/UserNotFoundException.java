package com.trevorgowing.projectlog.user;

public class UserNotFoundException extends RuntimeException {

  private static final long serialVersionUID = 6916075569670660878L;

  public static final String REASON = "User not found for id";

  private UserNotFoundException(long userId) {
    super(REASON + ": " + userId);
  }

  public static UserNotFoundException identifiedUserNotFoundException(long userId) {
    return new UserNotFoundException(userId);
  }
}
