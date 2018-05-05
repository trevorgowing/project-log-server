package com.trevorgowing.projectlog.log.issue;

import static com.trevorgowing.projectlog.log.issue.IdentifiedIssueDTO.completeIdentifiedIssueDTO;

import com.trevorgowing.projectlog.project.IdentifiedProjectDTO;
import com.trevorgowing.projectlog.project.ProjectDTOFactory;
import com.trevorgowing.projectlog.user.IdentifiedUserDTO;
import com.trevorgowing.projectlog.user.UserDTOFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IssueDTOFactory {

  private final UserDTOFactory userDTOFactory;
  private final ProjectDTOFactory projectDTOFactory;

  public IdentifiedIssueDTO createIdentifiedIssueDTO(Issue issue) {
    IdentifiedUserDTO identifiedUserDTO = userDTOFactory.createIdentifiedUserDTO(issue.getOwner());
    IdentifiedProjectDTO identifiedProjectDTO =
        projectDTOFactory.createIdentifiedProjectDTO(issue.getProject());

    return completeIdentifiedIssueDTO(
        issue.getId(),
        issue.getSummary(),
        issue.getDescription(),
        issue.getCategory(),
        issue.getImpact(),
        issue.getStatus(),
        issue.getDateClosed(),
        identifiedProjectDTO,
        identifiedUserDTO);
  }
}
