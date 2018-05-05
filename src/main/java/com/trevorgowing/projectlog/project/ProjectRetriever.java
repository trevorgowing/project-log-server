package com.trevorgowing.projectlog.project;

import static com.trevorgowing.projectlog.project.ProjectNotFoundException.identifiedProjectNotFoundException;
import static java.util.Optional.ofNullable;

import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ProjectRetriever {

  private final ProjectRepository projectRepository;

  public ProjectRetriever(ProjectRepository projectRepository) {
    this.projectRepository = projectRepository;
  }

  public Project findProject(long projectId) {
    return ofNullable(projectRepository.findOne(projectId))
        .orElseThrow(() -> identifiedProjectNotFoundException(projectId));
  }

  List<IdentifiedProjectDTO> getIdentifiedProjectDTOs() {
    return projectRepository.findIdentifiedProjectDTOs();
  }

  IdentifiedProjectDTO getIdentifiedProjectDTOById(long projectId) {
    return ofNullable(projectRepository.findIdentifiedProjectDTOById(projectId))
        .orElseThrow(() -> identifiedProjectNotFoundException(projectId));
  }
}
