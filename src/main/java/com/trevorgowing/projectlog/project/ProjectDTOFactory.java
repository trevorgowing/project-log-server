package com.trevorgowing.projectlog.project;

import static com.trevorgowing.projectlog.project.IdentifiedProjectDTO.completeIdentifiedProjectDTO;
import static com.trevorgowing.projectlog.user.IdentifiedUserDTO.passwordFreeIdentifiedUserDTO;

import com.trevorgowing.projectlog.user.User;
import org.springframework.stereotype.Service;

@Service
public class ProjectDTOFactory {

  public IdentifiedProjectDTO createIdentifiedProjectDTO(Project project) {
    User owner = project.getOwner();
    return completeIdentifiedProjectDTO(
        project.getId(),
        project.getCode(),
        project.getName(),
        passwordFreeIdentifiedUserDTO(
            owner.getId(), owner.getEmail(), owner.getFirstName(), owner.getLastName()),
        project.getStartDate(),
        project.getEndDate());
  }
}
