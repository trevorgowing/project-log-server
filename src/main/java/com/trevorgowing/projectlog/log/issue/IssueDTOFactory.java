package com.trevorgowing.projectlog.log.issue;

import com.trevorgowing.projectlog.project.IdentifiedProjectDTO;
import com.trevorgowing.projectlog.project.ProjectDTOFactory;
import com.trevorgowing.projectlog.user.IdentifiedUserDTO;
import com.trevorgowing.projectlog.user.UserDTOFactory;
import org.springframework.stereotype.Service;

import static com.trevorgowing.projectlog.log.issue.IdentifiedIssueDTO.completeIdentifiedIssueDTO;

@Service
public class IssueDTOFactory {

    private final UserDTOFactory userDTOFactory;
    private final ProjectDTOFactory projectDTOFactory;

    public IssueDTOFactory(UserDTOFactory userDTOFactory, ProjectDTOFactory projectDTOFactory) {
        this.userDTOFactory = userDTOFactory;
        this.projectDTOFactory = projectDTOFactory;
    }

    public IdentifiedIssueDTO createIdentifiedIssueDTO(Issue issue) {
        IdentifiedUserDTO identifiedUserDTO = userDTOFactory.createIdentifiedUserDTO(issue.getOwner());
        IdentifiedProjectDTO identifiedProjectDTO = projectDTOFactory.createIdentifiedProjectDTO(issue.getProject());

        return completeIdentifiedIssueDTO(issue.getId(), issue.getSummary(), issue.getDescription(),
                issue.getCategory(), issue.getImpact(), issue.getStatus(), issue.getDateClosed(), identifiedProjectDTO,
                identifiedUserDTO);
    }
}