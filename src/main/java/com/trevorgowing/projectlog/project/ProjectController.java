package com.trevorgowing.projectlog.project;

import com.trevorgowing.projectlog.project.constant.ProjectConstants;
import com.trevorgowing.projectlog.user.UserNotFoundException;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(ProjectConstants.PROJECTS_URL_PATH)
class ProjectController {

  private final ProjectFactory projectFactory;
  private final ProjectDeleter projectDeleter;
  private final ProjectModifier projectModifier;
  private final ProjectRetriever projectRetriever;
  private final ProjectDTOFactory projectDTOFactory;

  public ProjectController(
      ProjectFactory projectFactory,
      ProjectDeleter projectDeleter,
      ProjectModifier projectModifier,
      ProjectRetriever projectRetriever,
      ProjectDTOFactory projectDTOFactory) {
    this.projectFactory = projectFactory;
    this.projectDeleter = projectDeleter;
    this.projectModifier = projectModifier;
    this.projectRetriever = projectRetriever;
    this.projectDTOFactory = projectDTOFactory;
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  @ResponseStatus(HttpStatus.OK)
  List<IdentifiedProjectDTO> getProjects() {
    return projectRetriever.getIdentifiedProjectDTOs();
  }

  @GetMapping(
    path = ProjectConstants.PROJECT_ID_VARIABLE_URL_PATH,
    produces = MediaType.APPLICATION_JSON_UTF8_VALUE
  )
  @ResponseStatus(HttpStatus.OK)
  IdentifiedProjectDTO getProjectById(@PathVariable long projectId) {
    return projectRetriever.getIdentifiedProjectDTOById(projectId);
  }

  @PostMapping(
    consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
    produces = MediaType.APPLICATION_JSON_UTF8_VALUE
  )
  @ResponseStatus(HttpStatus.CREATED)
  IdentifiedProjectDTO postProject(@RequestBody UnidentifiedProjectDTO unidentifiedProjectDTO) {
    Project project =
        projectFactory.createProject(
            unidentifiedProjectDTO.getCode(),
            unidentifiedProjectDTO.getName(),
            unidentifiedProjectDTO.getOwner().getId(),
            unidentifiedProjectDTO.getStartDate(),
            unidentifiedProjectDTO.getEndDate());
    return projectDTOFactory.createIdentifiedProjectDTO(project);
  }

  @PutMapping(
    path = ProjectConstants.PROJECT_ID_VARIABLE_URL_PATH,
    consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
    produces = MediaType.APPLICATION_JSON_UTF8_VALUE
  )
  @ResponseStatus(HttpStatus.OK)
  IdentifiedProjectDTO putProject(
      @PathVariable long projectId, @RequestBody IdentifiedProjectDTO identifiedProjectDTO) {
    Project project =
        projectModifier.updateProject(
            projectId,
            identifiedProjectDTO.getCode(),
            identifiedProjectDTO.getName(),
            identifiedProjectDTO.getOwner().getId(),
            identifiedProjectDTO.getStartDate(),
            identifiedProjectDTO.getEndDate());
    return projectDTOFactory.createIdentifiedProjectDTO(project);
  }

  @DeleteMapping(path = ProjectConstants.PROJECT_ID_VARIABLE_URL_PATH)
  @ResponseStatus(HttpStatus.NO_CONTENT)
  void deleteProject(@PathVariable long projectId) {
    projectDeleter.deleteProject(projectId);
  }

  @ExceptionHandler(ProjectNotFoundException.class)
  @ResponseStatus(code = HttpStatus.NOT_FOUND, reason = ProjectNotFoundException.REASON)
  public void handleProjectNotFoundException(ProjectNotFoundException projectNotFoundException) {
    log.warn(projectNotFoundException.getMessage(), projectNotFoundException);
  }

  @ExceptionHandler(DuplicateProjectCodeException.class)
  @ResponseStatus(code = HttpStatus.CONFLICT, reason = DuplicateProjectCodeException.REASON)
  public void handleDuplicateCodeException(
      DuplicateProjectCodeException duplicateProjectCodeException) {
    log.warn(duplicateProjectCodeException.getMessage(), duplicateProjectCodeException);
  }

  @ExceptionHandler(UserNotFoundException.class)
  @ResponseStatus(code = HttpStatus.CONFLICT, reason = UserNotFoundException.REASON)
  public void handleUserNotFoundException(UserNotFoundException userNotFoundException) {
    log.warn(userNotFoundException.getMessage(), userNotFoundException);
  }
}
