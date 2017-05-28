package com.trevorgowing.projectlog.log.issue;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IssueCRUDService {

    private final IssueRepository issueRepository;

    public IssueCRUDService(IssueRepository issueRepository) {
        this.issueRepository = issueRepository;
    }

    public List<IdentifiedIssueDTO> getIdentifiedIssueDTOs() {
        return issueRepository.findIdentifiedIssueDTOs();
    }
}