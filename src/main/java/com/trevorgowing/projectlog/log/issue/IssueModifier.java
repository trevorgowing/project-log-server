package com.trevorgowing.projectlog.log.issue;

import com.trevorgowing.projectlog.project.ProjectRetriever;
import com.trevorgowing.projectlog.user.UserRetriever;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IssueModifier {

  private final UserRetriever userRetriever;
  private final IssueRetriever issueRetriever;
  private final IssueRepository issueRepository;
  private final ProjectRetriever projectRetriever;

  public Issue updateIssue(IdentifiedIssueDTO identifiedIssueDTO) {
    Issue issue = issueRetriever.findIssue(identifiedIssueDTO.getId());

    issue.setSummary(identifiedIssueDTO.getSummary());
    issue.setDescription(identifiedIssueDTO.getDescription());
    issue.setCategory(identifiedIssueDTO.getCategory());
    issue.setImpact(identifiedIssueDTO.getImpact());
    issue.setStatus(identifiedIssueDTO.getStatus());
    issue.setDateClosed(identifiedIssueDTO.getDateClosed());

    if (identifiedIssueDTO.getProject() != null
        && issue.getProject().getId() != identifiedIssueDTO.getProject().getId()) {
      issue.setProject(projectRetriever.findProject(identifiedIssueDTO.getProject().getId()));
    }

    if (identifiedIssueDTO.getOwner() != null
        && issue.getOwner().getId() != identifiedIssueDTO.getOwner().getId()) {
      issue.setOwner(userRetriever.findUser(identifiedIssueDTO.getOwner().getId()));
    }

    return issueRepository.save(issue);
  }
}
