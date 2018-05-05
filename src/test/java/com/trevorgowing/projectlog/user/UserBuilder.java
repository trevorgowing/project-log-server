package com.trevorgowing.projectlog.user;

import static com.trevorgowing.projectlog.user.User.identifiedUser;

import com.trevorgowing.projectlog.common.builders.AbstractEntityBuilder;
import com.trevorgowing.projectlog.common.persisters.AbstractEntityPersister;
import com.trevorgowing.projectlog.common.persisters.DefaultEntityPersister;

public class UserBuilder extends AbstractEntityBuilder<User> {

  private Long id;
  private String email;
  private String password = "Default Password";
  private String firstName;
  private String lastName;

  public static UserBuilder aUser() {
    return new UserBuilder();
  }

  @Override
  public User build() {
    return identifiedUser(id, email, password, firstName, lastName);
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
}
