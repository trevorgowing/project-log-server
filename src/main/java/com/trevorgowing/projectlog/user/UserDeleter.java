package com.trevorgowing.projectlog.user;

import static com.trevorgowing.projectlog.user.UserNotFoundException.identifiedUserNotFoundException;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

@Service
public class UserDeleter {

  private final UserRepository userRepository;

  public UserDeleter(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  void deleteUser(long userId) {
    try {
      userRepository.delete(userId);
    } catch (EmptyResultDataAccessException emptyResultDataAccessException) {
      throw identifiedUserNotFoundException(userId);
    }
  }
}
