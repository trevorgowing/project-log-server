package com.trevorgowing.projectlog.project;

import com.trevorgowing.projectlog.user.User;
import org.springframework.stereotype.Service;

import static com.trevorgowing.projectlog.project.IdentifiedProjectDTO.completeIdentifiedProjectDTO;
import static com.trevorgowing.projectlog.user.IdentifiedUserDTO.passwordFreeIdentifiedUserDTO;

@Service
class ProjectDTOFactory {

    IdentifiedProjectDTO createIdentifiedProjectDTO(Project project) {
        User owner = project.getOwner();
        return completeIdentifiedProjectDTO(project.getId(), project.getCode(), project.getName(),
                passwordFreeIdentifiedUserDTO(owner.getId(), owner.getEmail(), owner.getFirstName(),
                        owner.getLastName()), project.getStartDate(), project.getEndDate());
    }
}