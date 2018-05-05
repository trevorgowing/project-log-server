package com.trevorgowing.projectlog.user;

import static com.trevorgowing.projectlog.user.IdentifiedUserDTO.completeIdentifiedUserDTO;

import org.springframework.stereotype.Service;

@Service
public class UserDTOFactory {

  public IdentifiedUserDTO createIdentifiedUserDTO(User user) {
    return completeIdentifiedUserDTO(
        user.getId(), user.getEmail(), user.getPassword(), user.getFirstName(), user.getLastName());
  }
}
