package com.trevorgowing.projectlog.project;

import com.trevorgowing.projectlog.user.User;
import com.trevorgowing.projectlog.user.UserRetriever;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

import static com.trevorgowing.projectlog.project.DuplicateProjectCodeException.codedDuplicateCodeException;
import static com.trevorgowing.projectlog.project.Project.unidentifiedProject;
import static com.trevorgowing.projectlog.project.ProjectNotFoundException.identifiedProjectNotFoundException;
import static java.util.Optional.ofNullable;

@Service
public class ProjectCRUDService {

    private final UserRetriever userRetriever;
    private final ProjectRepository projectRepository;

    public ProjectCRUDService(UserRetriever userRetriever, ProjectRepository projectRepository) {
        this.userRetriever = userRetriever;
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

    Project createProject(String code, String name, long ownerId, LocalDate startDate, LocalDate endDate) {
        User owner = userRetriever.findUser(ownerId);

        Project project = unidentifiedProject(code, name, owner, startDate, endDate);

        try {
            return projectRepository.save(project);
        } catch (DataIntegrityViolationException dataIntegrityViolationException) {
            throw codedDuplicateCodeException(code);
        }
    }

    Project updateProject(long id, String code, String name, long ownerId, LocalDate startDate, LocalDate endDate) {
        Project projectToUpdate = findProject(id);

        projectToUpdate.setCode(code);
        projectToUpdate.setName(name);
        projectToUpdate.setOwner(userRetriever.findUser(ownerId));
        projectToUpdate.setStartDate(startDate);
        projectToUpdate.setEndDate(endDate);

        try {
            return projectRepository.save(projectToUpdate);
        } catch (DataIntegrityViolationException dataIntegrityViolationException) {
            throw codedDuplicateCodeException(code);
        }
    }

    void deleteProject(long projectId) {
        try {
            projectRepository.delete(projectId);
        } catch (EmptyResultDataAccessException emptyResultDataAccessException) {
            throw identifiedProjectNotFoundException(projectId);
        }
    }
}