package com.trevorgowing.projectlog.user;

import static com.trevorgowing.projectlog.user.DuplicateEmailException.causedDuplicateEmailException;
import static com.trevorgowing.projectlog.user.User.unidentifiedUser;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserFactory {

  private final UserRepository userRepository;

  User createUser(String email, String password, String firstName, String lastName) {
    try {
      return userRepository.save(unidentifiedUser(email, password, firstName, lastName));
    } catch (DataIntegrityViolationException dataIntegrityViolationException) {
      throw causedDuplicateEmailException(email, dataIntegrityViolationException);
    }
  }
}
