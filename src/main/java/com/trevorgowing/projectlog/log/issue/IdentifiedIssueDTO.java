package com.trevorgowing.projectlog.log.issue;

import static com.trevorgowing.projectlog.project.IdentifiedProjectDTO.minimalIdentifiedProjectDTO;
import static com.trevorgowing.projectlog.user.IdentifiedUserDTO.minimalIdentifiedUserDTO;

import com.trevorgowing.projectlog.log.constant.Category;
import com.trevorgowing.projectlog.log.constant.Impact;
import com.trevorgowing.projectlog.log.constant.LogStatus;
import com.trevorgowing.projectlog.project.IdentifiedProjectDTO;
import com.trevorgowing.projectlog.user.IdentifiedUserDTO;
import java.time.LocalDate;
import java.util.Objects;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
public class IdentifiedIssueDTO extends UnidentifiedIssueDTO {

  private static final long serialVersionUID = 9032983535827707811L;

  private long id;

  private IdentifiedIssueDTO(
      long id,
      String summary,
      String description,
      Category category,
      Impact impact,
      LogStatus status,
      LocalDate dateClosed,
      IdentifiedProjectDTO project,
      IdentifiedUserDTO owner) {
    super(summary, description, category, impact, status, dateClosed, project, owner);
    this.id = id;
  }

  /** Used in constructor queries {@link IssueRepository#findIdentifiedIssueDTOs()}. */
  @SuppressWarnings("unused")
  public IdentifiedIssueDTO(
      long id,
      String summary,
      String description,
      Category category,
      Impact impact,
      LogStatus status,
      LocalDate dateClosed,
      long projectId,
      String projectCode,
      String projectName,
      long ownerId,
      String ownerEmail,
      String ownerFirstName,
      String ownerLastName) {
    super(
        summary,
        description,
        category,
        impact,
        status,
        dateClosed,
        minimalIdentifiedProjectDTO(projectId, projectCode, projectName),
        minimalIdentifiedUserDTO(ownerId, ownerEmail, ownerFirstName, ownerLastName));
    this.id = id;
  }

  public static IdentifiedIssueDTO completeIdentifiedIssueDTO(
      long id,
      String summary,
      String description,
      Category category,
      Impact impact,
      LogStatus status,
      LocalDate dateClosed,
      IdentifiedProjectDTO project,
      IdentifiedUserDTO owner) {
    return new IdentifiedIssueDTO(
        id, summary, description, category, impact, status, dateClosed, project, owner);
  }

  @Override
  public boolean equals(Object objectToCompareTo) {
    if (this == objectToCompareTo) return true;
    if (objectToCompareTo == null || getClass() != objectToCompareTo.getClass()) return false;
    if (!super.equals(objectToCompareTo)) return false;
    IdentifiedIssueDTO identifiedIssueDTOToCompareTo = (IdentifiedIssueDTO) objectToCompareTo;
    return id == identifiedIssueDTOToCompareTo.id;
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), id);
  }
}
