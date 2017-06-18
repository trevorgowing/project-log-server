package com.trevorgowing.projectlog.log.issue;

import com.trevorgowing.projectlog.log.LogDTO;
import com.trevorgowing.projectlog.log.LogRetriever;
import com.trevorgowing.projectlog.project.Project;
import com.trevorgowing.projectlog.project.ProjectCRUDService;
import com.trevorgowing.projectlog.user.User;
import com.trevorgowing.projectlog.user.UserCRUDService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.trevorgowing.projectlog.log.issue.Issue.unidentifiedIssue;
import static com.trevorgowing.projectlog.log.issue.IssueNotFoundException.identifiedIssueNotFoundException;
import static java.util.Optional.ofNullable;

@Service
public class IssueCRUDService implements LogRetriever {

    private final IssueRepository issueRepository;
    private final UserCRUDService userCRUDService;
    private final ProjectCRUDService projectCRUDService;

    public IssueCRUDService(IssueRepository issueRepository, UserCRUDService userCRUDService,
                            ProjectCRUDService projectCRUDService) {
        this.issueRepository = issueRepository;
        this.userCRUDService = userCRUDService;
        this.projectCRUDService = projectCRUDService;
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

    public Issue createIssue(UnidentifiedIssueDTO unidentifiedIssueDTO) {
        Project project = projectCRUDService.findProject(unidentifiedIssueDTO.getProject().getId());
        User owner = userCRUDService.findUser(unidentifiedIssueDTO.getOwner().getId());

        Issue issue = unidentifiedIssue(unidentifiedIssueDTO.getSummary(), unidentifiedIssueDTO.getDescription(),
                unidentifiedIssueDTO.getCategory(), unidentifiedIssueDTO.getImpact(), unidentifiedIssueDTO.getStatus(),
                unidentifiedIssueDTO.getDateClosed(), project, owner);

        return issueRepository.save(issue);
    }
}