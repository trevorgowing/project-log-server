package com.trevorgowing.projectlog.project;

import static com.trevorgowing.projectlog.project.ProjectNotFoundException.identifiedProjectNotFoundException;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

@Service
public class ProjectDeleter {

  private final ProjectRepository projectRepository;

  public ProjectDeleter(ProjectRepository projectRepository) {
    this.projectRepository = projectRepository;
  }

  void deleteProject(long projectId) {
    try {
      projectRepository.delete(projectId);
    } catch (EmptyResultDataAccessException emptyResultDataAccessException) {
      throw identifiedProjectNotFoundException(projectId);
    }
  }
}
