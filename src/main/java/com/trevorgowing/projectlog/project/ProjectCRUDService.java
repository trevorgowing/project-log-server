package com.trevorgowing.projectlog.project;

import org.springframework.stereotype.Service;

import java.util.List;

import static com.trevorgowing.projectlog.project.ProjectNotFoundException.identifiedProjectNotFoundException;
import static java.util.Optional.ofNullable;

@Service
class ProjectCRUDService {

    private final ProjectRepository projectRepository;

    public ProjectCRUDService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    List<IdentifiedProjectDTO> getIdentifiedProjectDTOs() {
        return projectRepository.findIdentifiedProjectDTOs();
    }

    IdentifiedProjectDTO getIdentifiedProjectDTOById(long projectId) {
        return ofNullable(projectRepository.findIdentifiedProjectDTOById(projectId))
                .orElseThrow(() -> identifiedProjectNotFoundException(projectId));
    }
}