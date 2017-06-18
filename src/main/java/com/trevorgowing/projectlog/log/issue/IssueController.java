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

    private final IssueDTOFactory issueDTOFactory;
    private final IssueCRUDService issueCRUDService;

    IssueController(IssueDTOFactory issueDTOFactory, IssueCRUDService issueCRUDService) {
        this.issueDTOFactory = issueDTOFactory;
        this.issueCRUDService = issueCRUDService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    IdentifiedIssueDTO postIssue(@RequestBody UnidentifiedIssueDTO unidentifiedIssueDTO) {
        Issue issue = issueCRUDService.createIssue(unidentifiedIssueDTO);
        return issueDTOFactory.createIdentifiedIssueDTO(issue);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.OK)
    IdentifiedIssueDTO putIssue(@RequestBody IdentifiedIssueDTO identifiedIssueDTO) {
        Issue issue = issueCRUDService.updateIssue(identifiedIssueDTO);
        return issueDTOFactory.createIdentifiedIssueDTO(issue);
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