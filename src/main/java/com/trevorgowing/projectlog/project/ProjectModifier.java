package com.trevorgowing.projectlog.project;

import com.trevorgowing.projectlog.user.UserRetriever;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

import static com.trevorgowing.projectlog.project.DuplicateProjectCodeException.codedDuplicateCodeException;

@Service
public class ProjectModifier {

    private final UserRetriever userRetriever;
    private final ProjectRetriever projectRetriever;
    private final ProjectRepository projectRepository;

    public ProjectModifier(UserRetriever userRetriever, ProjectRetriever projectRetriever,
                           ProjectRepository projectRepository) {
        this.userRetriever = userRetriever;
        this.projectRetriever = projectRetriever;
        this.projectRepository = projectRepository;
    }

    Project updateProject(long id, String code, String name, long ownerId, LocalDate startDate, LocalDate endDate) {
        Project projectToUpdate = projectRetriever.findProject(id);

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
}