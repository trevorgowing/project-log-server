package com.trevorgowing.projectlog.user;

import com.trevorgowing.projectlog.common.builders.DomainObjectBuilder;

public final class DuplicateUserExceptionBuilder implements DomainObjectBuilder<DuplicateUserException> {

    private String email;

    public static DuplicateUserExceptionBuilder aDuplicateUserException() {
        return new DuplicateUserExceptionBuilder();
    }

    @Override
    public DuplicateUserException build() {
        return new DuplicateUserException(email);
    }

    public DuplicateUserExceptionBuilder withEmail(String email) {
        this.email = email;
        return this;
    }
}
