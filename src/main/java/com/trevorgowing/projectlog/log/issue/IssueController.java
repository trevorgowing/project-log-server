package com.trevorgowing.projectlog.log.issue;

import com.trevorgowing.projectlog.log.constant.LogConstants;
import com.trevorgowing.projectlog.project.ProjectNotFoundException;
import com.trevorgowing.projectlog.user.UserNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(LogConstants.LOGS_URL_PATH + LogConstants.ISSUES_URL_PATH)
class IssueController {

    private final IssueFactory issueFactory;
    private final IssueModifier issueModifier;
    private final IssueDTOFactory issueDTOFactory;

    IssueController(IssueFactory issueFactory, IssueDTOFactory issueDTOFactory, IssueModifier issueModifier) {
        this.issueFactory = issueFactory;
        this.issueDTOFactory = issueDTOFactory;
        this.issueModifier = issueModifier;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    IdentifiedIssueDTO postIssue(@RequestBody UnidentifiedIssueDTO unidentifiedIssueDTO) {
        Issue issue = issueFactory.createIssue(unidentifiedIssueDTO);
        return issueDTOFactory.createIdentifiedIssueDTO(issue);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.OK)
    IdentifiedIssueDTO putIssue(@RequestBody IdentifiedIssueDTO identifiedIssueDTO) {
        Issue issue = issueModifier.updateIssue(identifiedIssueDTO);
        return issueDTOFactory.createIdentifiedIssueDTO(issue);
    }

    @ExceptionHandler(IssueNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void handleIssueNotFoundException(IssueNotFoundException issueNotFoundException) {
        log.warn(issueNotFoundException.getMessage(), issueNotFoundException);
    }

    @ExceptionHandler(ProjectNotFoundException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public void handleProjectNotFoundException(ProjectNotFoundException projectNotFoundException) {
        log.warn(projectNotFoundException.getMessage(), projectNotFoundException);
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public void handleUserNotFoundException(UserNotFoundException userNotFoundException) {
        log.warn(userNotFoundException.getMessage(), userNotFoundException);
    }
}