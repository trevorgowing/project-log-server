package com.trevorgowing.projectlog.log.issue;

import static com.trevorgowing.projectlog.log.issue.IssueNotFoundException.identifiedIssueNotFoundException;
import static java.util.Optional.ofNullable;

import com.trevorgowing.projectlog.log.LogDTO;
import com.trevorgowing.projectlog.log.LogRetriever;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class IssueRetriever implements LogRetriever {

  private final IssueRepository issueRepository;

  public IssueRetriever(IssueRepository issueRepository) {
    this.issueRepository = issueRepository;
  }

  public Issue findIssue(long issueId) {
    return ofNullable(issueRepository.findOne(issueId))
        .orElseThrow(() -> identifiedIssueNotFoundException(issueId));
  }

  @Override
  public List<LogDTO> getLogDTOs() {
    return new ArrayList<>(getIdentifiedIssueDTOs());
  }

  @Override
  public LogDTO getLogDTOById(long id) {
    return ofNullable(issueRepository.findIdentifiedIssueDTOById(id))
        .orElseThrow(() -> identifiedIssueNotFoundException(id));
  }

  List<IdentifiedIssueDTO> getIdentifiedIssueDTOs() {
    return issueRepository.findIdentifiedIssueDTOs();
  }
}
