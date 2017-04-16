package com.trevorgowing.projectlog.project;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
class ProjectCRUDService {

    private final ProjectRepository projectRepository;

    public ProjectCRUDService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    List<IdentifiedProjectDTO> getIdentifiedProjectDTOs() {
        return projectRepository.findIdentifiedProjectDTOs();
    }
}