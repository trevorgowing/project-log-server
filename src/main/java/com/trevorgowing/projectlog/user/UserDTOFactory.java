package com.trevorgowing.projectlog.user;

import org.springframework.stereotype.Service;

import static com.trevorgowing.projectlog.user.UserResponseDTO.completeUserResponseDTO;

@Service
class UserDTOFactory {

    UserResponseDTO createUserResponseDTO(User user) {
        return completeUserResponseDTO(user.getId(), user.getEmail(), user.getPassword(), user.getFirstName(),
                user.getLastName());
    }
}
