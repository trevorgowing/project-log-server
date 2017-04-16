package com.trevorgowing.projectlog.project;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(ProjectConstants.PROJECTS_URL_PATH)
class ProjectController {

    private final ProjectCRUDService projectCRUDService;

    ProjectController(ProjectCRUDService projectCRUDService) {
        this.projectCRUDService = projectCRUDService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.OK)
    List<IdentifiedProjectDTO> getProjects() {
        return projectCRUDService.getIdentifiedProjectDTOs();
    }

    @GetMapping(path = ProjectConstants.PROJECT_ID_VARIABLE_URL_PATH, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.OK)
    IdentifiedProjectDTO getProjectById(@PathVariable long projectId) {
        return projectCRUDService.getIdentifiedProjectDTOById(projectId);
    }

    @ExceptionHandler(ProjectNotFoundException.class)
    @ResponseStatus(code = HttpStatus.NOT_FOUND, reason = ProjectNotFoundException.REASON)
    public void handleProjectNotFoundException(ProjectNotFoundException projectNotFoundException) {
        log.warn(projectNotFoundException.getMessage(), projectNotFoundException);
    }
}