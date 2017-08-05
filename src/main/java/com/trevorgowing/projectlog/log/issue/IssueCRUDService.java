package com.trevorgowing.projectlog.log.issue;

import com.trevorgowing.projectlog.log.LogDTO;
import com.trevorgowing.projectlog.log.LogRetriever;
import com.trevorgowing.projectlog.project.Project;
import com.trevorgowing.projectlog.project.ProjectCRUDService;
import com.trevorgowing.projectlog.user.User;
import com.trevorgowing.projectlog.user.UserRetriever;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.trevorgowing.projectlog.log.issue.Issue.unidentifiedIssue;
import static com.trevorgowing.projectlog.log.issue.IssueNotFoundException.identifiedIssueNotFoundException;
import static java.util.Optional.ofNullable;

@Service
public class IssueCRUDService implements LogRetriever {

    private final UserRetriever userRetriever;
    private final IssueRepository issueRepository;
    private final ProjectCRUDService projectCRUDService;

    public IssueCRUDService(UserRetriever userRetriever, IssueRepository issueRepository,
                            ProjectCRUDService projectCRUDService) {
        this.userRetriever = userRetriever;
        this.issueRepository = issueRepository;
        this.projectCRUDService = projectCRUDService;
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

    public Issue createIssue(UnidentifiedIssueDTO unidentifiedIssueDTO) {
        Project project = projectCRUDService.findProject(unidentifiedIssueDTO.getProject().getId());
        User owner = userRetriever.findUser(unidentifiedIssueDTO.getOwner().getId());

        Issue issue = unidentifiedIssue(unidentifiedIssueDTO.getSummary(), unidentifiedIssueDTO.getDescription(),
                unidentifiedIssueDTO.getCategory(), unidentifiedIssueDTO.getImpact(), unidentifiedIssueDTO.getStatus(),
                unidentifiedIssueDTO.getDateClosed(), project, owner);

        return issueRepository.save(issue);
    }

    public Issue updateIssue(IdentifiedIssueDTO identifiedIssueDTO) {
        Issue issue = findIssue(identifiedIssueDTO.getId());

        issue.setSummary(identifiedIssueDTO.getSummary());
        issue.setDescription(identifiedIssueDTO.getDescription());
        issue.setCategory(identifiedIssueDTO.getCategory());
        issue.setImpact(identifiedIssueDTO.getImpact());
        issue.setStatus(identifiedIssueDTO.getStatus());
        issue.setDateClosed(identifiedIssueDTO.getDateClosed());

        if (identifiedIssueDTO.getProject() != null
                && issue.getProject().getId() != identifiedIssueDTO.getProject().getId()) {
            issue.setProject(projectCRUDService.findProject(identifiedIssueDTO.getProject().getId()));
        }

        if (identifiedIssueDTO.getOwner() != null
                && issue.getOwner().getId() != identifiedIssueDTO.getOwner().getId()) {
            issue.setOwner(userRetriever.findUser(identifiedIssueDTO.getOwner().getId()));
        }

        return issueRepository.save(issue);
    }
}