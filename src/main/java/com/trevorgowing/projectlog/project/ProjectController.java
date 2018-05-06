package com.trevorgowing.projectlog.project;

import static com.trevorgowing.projectlog.project.constant.ProjectConstants.PROJECTS_URL_PATH;
import static com.trevorgowing.projectlog.project.constant.ProjectConstants.PROJECT_ID_VARIABLE_URL_PATH;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

import com.trevorgowing.projectlog.common.exception.ExceptionResponse;
import com.trevorgowing.projectlog.user.UserNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
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
@RequiredArgsConstructor
@RequestMapping(PROJECTS_URL_PATH)
class ProjectController {

  private final ProjectFactory projectFactory;
  private final ProjectDeleter projectDeleter;
  private final ProjectModifier projectModifier;
  private final ProjectRetriever projectRetriever;
  private final ProjectDTOFactory projectDTOFactory;

  @GetMapping(produces = APPLICATION_JSON_UTF8_VALUE)
  @ResponseStatus(OK)
  List<IdentifiedProjectDTO> getProjects() {
    return projectRetriever.getIdentifiedProjectDTOs();
  }

  @GetMapping(path = PROJECT_ID_VARIABLE_URL_PATH, produces = APPLICATION_JSON_UTF8_VALUE)
  @ResponseStatus(OK)
  IdentifiedProjectDTO getProjectById(@PathVariable long projectId) {
    return projectRetriever.getIdentifiedProjectDTOById(projectId);
  }

  @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE, produces = APPLICATION_JSON_UTF8_VALUE)
  @ResponseStatus(CREATED)
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
    path = PROJECT_ID_VARIABLE_URL_PATH,
    consumes = APPLICATION_JSON_UTF8_VALUE,
    produces = APPLICATION_JSON_UTF8_VALUE
  )
  @ResponseStatus(OK)
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

  @DeleteMapping(path = PROJECT_ID_VARIABLE_URL_PATH)
  @ResponseStatus(NO_CONTENT)
  void deleteProject(@PathVariable long projectId) {
    projectDeleter.deleteProject(projectId);
  }

  @ExceptionHandler(ProjectNotFoundException.class)
  public ResponseEntity<ExceptionResponse> handleProjectNotFoundException(
      ProjectNotFoundException projectNotFoundException) {
    log.debug(projectNotFoundException.getMessage(), projectNotFoundException);
    return ResponseEntity.status(NOT_FOUND)
        .contentType(APPLICATION_JSON_UTF8)
        .body(ExceptionResponse.from(NOT_FOUND, projectNotFoundException.getMessage()));
  }

  @ExceptionHandler(DuplicateProjectCodeException.class)
  public ResponseEntity<ExceptionResponse> handleDuplicateCodeException(
      DuplicateProjectCodeException duplicateProjectCodeException) {
    log.debug(duplicateProjectCodeException.getMessage(), duplicateProjectCodeException);
    return ResponseEntity.status(CONFLICT)
        .contentType(APPLICATION_JSON_UTF8)
        .body(ExceptionResponse.from(CONFLICT, duplicateProjectCodeException.getMessage()));
  }

  @ExceptionHandler(UserNotFoundException.class)
  public ResponseEntity<ExceptionResponse> handleUserNotFoundException(
      UserNotFoundException userNotFoundException) {
    log.debug(userNotFoundException.getMessage(), userNotFoundException);
    return ResponseEntity.status(CONFLICT)
        .contentType(APPLICATION_JSON_UTF8)
        .body(ExceptionResponse.from(CONFLICT, userNotFoundException.getMessage()));
  }
}
