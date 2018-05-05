package com.trevorgowing.projectlog.project;

import static com.trevorgowing.projectlog.project.ProjectNotFoundException.identifiedProjectNotFoundException;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProjectDeleter {

  private final ProjectRepository projectRepository;

  void deleteProject(long projectId) {
    try {
      projectRepository.deleteById(projectId);
    } catch (EmptyResultDataAccessException emptyResultDataAccessException) {
      throw identifiedProjectNotFoundException(projectId);
    }
  }
}
