package com.trevorgowing.projectlog.project;

import static com.trevorgowing.projectlog.project.DuplicateProjectCodeException.codedDuplicateCodeException;
import static com.trevorgowing.projectlog.project.Project.unidentifiedProject;

import com.trevorgowing.projectlog.user.User;
import com.trevorgowing.projectlog.user.UserRetriever;
import java.time.LocalDate;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
public class ProjectFactory {

  private final UserRetriever userRetriever;
  private final ProjectRepository projectRepository;

  public ProjectFactory(UserRetriever userRetriever, ProjectRepository projectRepository) {
    this.userRetriever = userRetriever;
    this.projectRepository = projectRepository;
  }

  Project createProject(
      String code, String name, long ownerId, LocalDate startDate, LocalDate endDate) {
    User owner = userRetriever.findUser(ownerId);

    Project project = unidentifiedProject(code, name, owner, startDate, endDate);

    try {
      return projectRepository.save(project);
    } catch (DataIntegrityViolationException dataIntegrityViolationException) {
      throw codedDuplicateCodeException(code);
    }
  }
}
