package com.trevorgowing.projectlog.user;

import java.io.Serializable;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class UnidentifiedUserDTO implements Serializable {

  private static final long serialVersionUID = 1507127704121652160L;

  protected String email;
  protected String password;
  protected String firstName;
  protected String lastName;

  public static UnidentifiedUserDTO completeUnidentifiedUser(
      String email, String password, String firstName, String lastName) {
    return new UnidentifiedUserDTO(email, password, firstName, lastName);
  }

  @Override
  public boolean equals(Object objectToCompareTo) {
    if (this == objectToCompareTo) return true;
    if (objectToCompareTo == null || getClass() != objectToCompareTo.getClass()) return false;
    UnidentifiedUserDTO unidentifiedUserDTOToCompareTo = (UnidentifiedUserDTO) objectToCompareTo;
    return Objects.equals(email, unidentifiedUserDTOToCompareTo.email)
        && Objects.equals(password, unidentifiedUserDTOToCompareTo.password)
        && Objects.equals(firstName, unidentifiedUserDTOToCompareTo.firstName)
        && Objects.equals(lastName, unidentifiedUserDTOToCompareTo.lastName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(email, password, firstName, lastName);
  }
}
