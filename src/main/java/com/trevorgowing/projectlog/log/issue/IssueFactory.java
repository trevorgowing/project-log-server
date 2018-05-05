package com.trevorgowing.projectlog.log.issue;

import static com.trevorgowing.projectlog.log.issue.Issue.unidentifiedIssue;

import com.trevorgowing.projectlog.project.Project;
import com.trevorgowing.projectlog.project.ProjectRetriever;
import com.trevorgowing.projectlog.user.User;
import com.trevorgowing.projectlog.user.UserRetriever;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IssueFactory {

  private final UserRetriever userRetriever;
  private final IssueRepository issueRepository;
  private final ProjectRetriever projectRetriever;

  public Issue createIssue(UnidentifiedIssueDTO unidentifiedIssueDTO) {
    Project project = projectRetriever.findProject(unidentifiedIssueDTO.getProject().getId());
    User owner = userRetriever.findUser(unidentifiedIssueDTO.getOwner().getId());

    Issue issue =
        unidentifiedIssue(
            unidentifiedIssueDTO.getSummary(),
            unidentifiedIssueDTO.getDescription(),
            unidentifiedIssueDTO.getCategory(),
            unidentifiedIssueDTO.getImpact(),
            unidentifiedIssueDTO.getStatus(),
            unidentifiedIssueDTO.getDateClosed(),
            project,
            owner);

    return issueRepository.save(issue);
  }
}
