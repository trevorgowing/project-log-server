package com.trevorgowing.projectlog.user;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.trevorgowing.projectlog.user.DuplicateEmailException.causedDuplicateEmailException;
import static com.trevorgowing.projectlog.user.User.unidentifiedUser;
import static com.trevorgowing.projectlog.user.UserNotFoundException.identifiedUserNotFoundException;
import static java.util.Optional.ofNullable;

@Service
class UserCRUDService {

    private final UserRepository userRepository;

    UserCRUDService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    List<IdentifiedUserDTO> findIdentifiedUserDTOs() {
        return userRepository.findIdentifiedUserDTOs();
    }

    IdentifiedUserDTO findIdentifiedUserDTOById(long userId) {
        return ofNullable(userRepository.findIdentifiedUserDTOById(userId))
                .orElseThrow(() -> identifiedUserNotFoundException(userId));
    }

    User createUser(String email, String password, String firstName, String lastName) {
        try {
            return userRepository.save(unidentifiedUser(email, password, firstName, lastName));
        } catch (DataIntegrityViolationException dataIntegrityViolationException) {
            throw causedDuplicateEmailException(email, dataIntegrityViolationException);
        }
    }

    User updateUser(long userId, String email, String password, String firstName, String lastName) {
        Optional<User> optionalUserToUpdate = ofNullable(userRepository.findOne(userId));
        User userToUpdate = optionalUserToUpdate.orElseThrow(() -> identifiedUserNotFoundException(userId));

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

    void deleteUser(long userId) {
        try {
            userRepository.delete(userId);
        } catch (EmptyResultDataAccessException emptyResultDataAccessException) {
            throw identifiedUserNotFoundException(userId);
        }
    }
}
