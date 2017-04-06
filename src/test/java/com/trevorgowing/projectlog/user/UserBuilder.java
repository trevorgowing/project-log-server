package com.trevorgowing.projectlog.user;

import com.trevorgowing.projectlog.common.builders.AbstractEntityBuilder;
import com.trevorgowing.projectlog.common.persisters.AbstractEntityPersister;
import com.trevorgowing.projectlog.common.persisters.DefaultEntityPersister;

import java.time.Instant;

public class UserBuilder extends AbstractEntityBuilder<User> {

    private Long id;
    private String email;
    private String password;
    private String firstName;
    private String lastName;

    private User createdBy;
    private Instant createdDate = Instant.now();
    private User lastModifiedBy;
    private Instant lastModifiedDate;

    public static UserBuilder aUser() {
        return new UserBuilder();
    }

    @Override
    public User build() {
        User user = User.identifiedUser(id, email, password, firstName, lastName);
        user.setCreatedBy(createdBy);
        user.setCreatedDate(createdDate);
        user.setLastModifiedBy(lastModifiedBy);
        user.setLastModifiedDate(lastModifiedDate);
        return user;
    }

    @Override
    public AbstractEntityPersister<User> getPersister() {
        return new DefaultEntityPersister<>();
    }

    public UserBuilder id(long id) {
        this.id = id;
        return this;
    }

    public UserBuilder email(String email) {
        this.email = email;
        return this;
    }

    public UserBuilder password(String password) {
        this.password = password;
        return this;
    }

    public UserBuilder firstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public UserBuilder lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public UserBuilder createdBy(User createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public UserBuilder createdDate(Instant createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public UserBuilder lastModifiedBy(User lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
        return this;
    }

    public UserBuilder lastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
        return this;
    }
}
