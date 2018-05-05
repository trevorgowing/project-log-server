package com.trevorgowing.projectlog.project;

import com.trevorgowing.projectlog.user.IdentifiedUserDTO;
import java.io.Serializable;
import java.time.LocalDate;
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
public class UnidentifiedProjectDTO implements Serializable {

  private static final long serialVersionUID = -6688518114783518949L;

  protected String code;
  protected String name;
  protected IdentifiedUserDTO owner;
  protected LocalDate startDate;
  protected LocalDate endDate;

  public static UnidentifiedProjectDTO completeUnidentifiedProjectDTO(
      String code, String name, IdentifiedUserDTO owner, LocalDate startDate, LocalDate endDate) {
    return new UnidentifiedProjectDTO(code, name, owner, startDate, endDate);
  }

  @Override
  public boolean equals(Object objectToCompareTo) {
    if (this == objectToCompareTo) return true;
    if (objectToCompareTo == null || getClass() != objectToCompareTo.getClass()) return false;
    UnidentifiedProjectDTO unidentifiedProjectDTOToCompareTo =
        (UnidentifiedProjectDTO) objectToCompareTo;
    return Objects.equals(code, unidentifiedProjectDTOToCompareTo.code)
        && Objects.equals(owner, unidentifiedProjectDTOToCompareTo.owner)
        && Objects.equals(startDate, unidentifiedProjectDTOToCompareTo.startDate)
        && Objects.equals(endDate, unidentifiedProjectDTOToCompareTo.endDate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(code, owner, startDate, endDate);
  }
}
