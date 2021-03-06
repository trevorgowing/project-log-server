package com.trevorgowing.projectlog.user;

import com.trevorgowing.projectlog.common.persistence.AbstractAuditable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "users")
@ToString(callSuper = true)
public class User extends AbstractAuditable<User, Long> {

  private static final long serialVersionUID = 2138197451190291396L;

  @Basic(optional = false)
  @Column(nullable = false, unique = true)
  private String email;

  @Basic(optional = false)
  @Column(nullable = false)
  private String password;

  private String firstName;

  private String lastName;

  private User(String email, String password, String firstName, String lastName) {
    setAll(email, password, firstName, lastName);
  }

  private User(Long id, String email, String password, String firstName, String lastName) {
    super(id);
    setAll(email, password, firstName, lastName);
  }

  private void setAll(String email, String password, String firstName, String lastName) {
    this.email = email;
    this.password = password;
    this.firstName = firstName;
    this.lastName = lastName;
  }

  public static User unidentifiedUser(
      String email, String password, String firstName, String lastName) {
    return new User(email, password, firstName, lastName);
  }

  public static User identifiedUser(
      Long id, String email, String password, String firstName, String lastName) {
    return new User(id, email, password, firstName, lastName);
  }

  public String getName() {
    return firstName + " " + lastName;
  }
}
