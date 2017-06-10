package com.trevorgowing.projectlog.log.issue;

import com.trevorgowing.projectlog.log.LogDTO;
import com.trevorgowing.projectlog.log.LogRetriever;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.trevorgowing.projectlog.log.issue.IssueNotFoundException.identifiedIssueNotFoundException;
import static java.util.Optional.ofNullable;

@Service
public class IssueCRUDService implements LogRetriever {

    private final IssueRepository issueRepository;

    public IssueCRUDService(IssueRepository issueRepository) {
        this.issueRepository = issueRepository;
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