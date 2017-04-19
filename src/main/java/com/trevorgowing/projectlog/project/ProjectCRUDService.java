package com.trevorgowing.projectlog.project;

import com.trevorgowing.projectlog.user.User;
import com.trevorgowing.projectlog.user.UserCRUDService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

import static com.trevorgowing.projectlog.project.DuplicateProjectCodeException.codedDuplicateCodeException;
import static com.trevorgowing.projectlog.project.Project.unidentifiedProject;
import static com.trevorgowing.projectlog.project.ProjectNotFoundException.identifiedProjectNotFoundException;
import static java.util.Optional.ofNullable;

@Service
class ProjectCRUDService {

    private final UserCRUDService userCRUDService;
    private final ProjectRepository projectRepository;

    public ProjectCRUDService(UserCRUDService userCRUDService, ProjectRepository projectRepository) {
        this.userCRUDService = userCRUDService;
        this.projectRepository = projectRepository;
    }

    List<IdentifiedProjectDTO> getIdentifiedProjectDTOs() {
        return projectRepository.findIdentifiedProjectDTOs();
    }

    IdentifiedProjectDTO getIdentifiedProjectDTOById(long projectId) {
        return ofNullable(projectRepository.findIdentifiedProjectDTOById(projectId))
                .orElseThrow(() -> identifiedProjectNotFoundException(projectId));
    }

    Project createProject(String code, String name, long ownerId, LocalDate startDate, LocalDate endDate) {
        User user = userCRUDService.findUser(ownerId);

        Project project = unidentifiedProject(code, name, user, startDate, endDate);

        try {
            return projectRepository.save(project);
        } catch (DataIntegrityViolationException dataIntegrityViolationException) {
            throw codedDuplicateCodeException(code);
        }
    }
}