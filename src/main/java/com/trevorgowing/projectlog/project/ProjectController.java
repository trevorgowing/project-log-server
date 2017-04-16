package com.trevorgowing.projectlog.project;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
}