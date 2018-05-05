package com.trevorgowing.projectlog.user;

import static com.trevorgowing.projectlog.user.DuplicateEmailException.causedDuplicateEmailException;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
public class UserModifier {

  private final UserRetriever userRetriever;
  private final UserRepository userRepository;

  UserModifier(UserRetriever userRetriever, UserRepository userRepository) {
    this.userRetriever = userRetriever;
    this.userRepository = userRepository;
  }

  User updateUser(long userId, String email, String password, String firstName, String lastName) {
    User userToUpdate = userRetriever.findUser(userId);

    userToUpdate.setEmail(email);
    userToUpdate.setPassword(password);
    userToUpdate.setFirstName(firstName);
    userToUpdate.setLastName(lastName);

    try {
      return userRepository.save(userToUpdate);
    } catch (DataIntegrityViolationException dataIntegrityViolationException) {
      throw causedDuplicateEmailException(email, dataIntegrityViolationException);
    }
  }
}
