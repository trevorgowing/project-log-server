package com.trevorgowing.projectlog.user;

import org.springframework.stereotype.Service;

import static com.trevorgowing.projectlog.user.IdentifiedUserDTO.completeIdentifiedUserDTO;

@Service
public class UserDTOFactory {

    public IdentifiedUserDTO createIdentifiedUserDTO(User user) {
        return completeIdentifiedUserDTO(user.getId(), user.getEmail(), user.getPassword(), user.getFirstName(),
                user.getLastName());
    }
}
