package com.trevorgowing.projectlog.project;

import com.trevorgowing.projectlog.user.UserNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(ProjectConstants.PROJECTS_URL_PATH)
class ProjectController {

    private final ProjectDTOFactory projectDTOFactory;
    private final ProjectCRUDService projectCRUDService;

    ProjectController(ProjectDTOFactory projectDTOFactory, ProjectCRUDService projectCRUDService) {
        this.projectDTOFactory = projectDTOFactory;
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

    @PostMapping(consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    IdentifiedProjectDTO postProject(@RequestBody UnidentifiedProjectDTO unidentifiedProjectDTO) {
        Project project = projectCRUDService.createProject(unidentifiedProjectDTO.getCode(),unidentifiedProjectDTO.getName(),
                unidentifiedProjectDTO.getOwner().getId(), unidentifiedProjectDTO.getStartDate(),
                unidentifiedProjectDTO.getEndDate());
        return projectDTOFactory.createIdentifiedProjectDTO(project);
    }

    @PutMapping(path = ProjectConstants.PROJECT_ID_VARIABLE_URL_PATH, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.OK)
    IdentifiedProjectDTO putProject(@PathVariable long projectId,
                                    @RequestBody IdentifiedProjectDTO identifiedProjectDTO) {
        Project project = projectCRUDService.updateProject(projectId, identifiedProjectDTO.getCode(),
                identifiedProjectDTO.getName(), identifiedProjectDTO.getOwner().getId(),
                identifiedProjectDTO.getStartDate(), identifiedProjectDTO.getEndDate());
         return projectDTOFactory.createIdentifiedProjectDTO(project);
    }

    @DeleteMapping(path = ProjectConstants.PROJECT_ID_VARIABLE_URL_PATH)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteProject(@PathVariable long projectId) {
        projectCRUDService.deleteProject(projectId);
    }

    @ExceptionHandler(ProjectNotFoundException.class)
    @ResponseStatus(code = HttpStatus.NOT_FOUND, reason = ProjectNotFoundException.REASON)
    public void handleProjectNotFoundException(ProjectNotFoundException projectNotFoundException) {
        log.warn(projectNotFoundException.getMessage(), projectNotFoundException);
    }

    @ExceptionHandler(DuplicateProjectCodeException.class)
    @ResponseStatus(code = HttpStatus.CONFLICT, reason = DuplicateProjectCodeException.REASON)
    public void handleDuplicateCodeException(DuplicateProjectCodeException duplicateProjectCodeException) {
        log.warn(duplicateProjectCodeException.getMessage(), duplicateProjectCodeException);

    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(code = HttpStatus.CONFLICT, reason = UserNotFoundException.REASON)
    public void handleUserNotFoundException(UserNotFoundException userNotFoundException) {
        log.warn(userNotFoundException.getMessage(), userNotFoundException);
    }
}