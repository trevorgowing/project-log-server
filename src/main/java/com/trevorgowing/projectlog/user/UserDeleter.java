package com.trevorgowing.projectlog.user;

import static com.trevorgowing.projectlog.user.UserNotFoundException.identifiedUserNotFoundException;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDeleter {

  private final UserRepository userRepository;

  void deleteUser(long userId) {
    try {
      userRepository.delete(userId);
    } catch (EmptyResultDataAccessException emptyResultDataAccessException) {
      throw identifiedUserNotFoundException(userId);
    }
  }
}
