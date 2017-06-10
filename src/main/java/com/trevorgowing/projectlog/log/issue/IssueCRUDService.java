package com.trevorgowing.projectlog.log.issue;

import com.trevorgowing.projectlog.log.LogCRUDService;
import com.trevorgowing.projectlog.log.LogDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class IssueCRUDService implements LogCRUDService {

    private final IssueRepository issueRepository;

    public IssueCRUDService(IssueRepository issueRepository) {
        this.issueRepository = issueRepository;
    }

    @Override
    public List<LogDTO> getLogDTOs() {
        return new ArrayList<>(getIdentifiedIssueDTOs());
    }

    public List<IdentifiedIssueDTO> getIdentifiedIssueDTOs() {
        return issueRepository.findIdentifiedIssueDTOs();
    }
}