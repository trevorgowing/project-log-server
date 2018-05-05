package com.trevorgowing.projectlog.user;

import static com.trevorgowing.projectlog.user.DuplicateEmailException.causedDuplicateEmailException;

import com.trevorgowing.projectlog.common.builders.DomainObjectBuilder;

public final class DuplicateEmailExceptionBuilder
    implements DomainObjectBuilder<DuplicateEmailException> {

  private String email;
  private Throwable cause = new RuntimeException("Root cause exception");

  public static DuplicateEmailExceptionBuilder aDuplicateEmailException() {
    return new DuplicateEmailExceptionBuilder();
  }

  @Override
  public DuplicateEmailException build() {
    return causedDuplicateEmailException(email, cause);
  }

  public DuplicateEmailExceptionBuilder withEmail(String email) {
    this.email = email;
    return this;
  }
}
