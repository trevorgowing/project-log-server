package com.trevorgowing.projectlog.user;

import static com.trevorgowing.projectlog.user.UserNotFoundException.identifiedUserNotFoundException;
import static java.util.Optional.ofNullable;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserRetriever {

  private final UserRepository userRepository;

  public User findUser(long userId) {
    return ofNullable(userRepository.findOne(userId))
        .orElseThrow(() -> identifiedUserNotFoundException(userId));
  }

  List<IdentifiedUserDTO> findIdentifiedUserDTOs() {
    return userRepository.findIdentifiedUserDTOs();
  }

  IdentifiedUserDTO findIdentifiedUserDTOById(long userId) {
    return ofNullable(userRepository.findIdentifiedUserDTOById(userId))
        .orElseThrow(() -> identifiedUserNotFoundException(userId));
  }
}
