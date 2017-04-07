package com.trevorgowing.projectlog.user;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.trevorgowing.projectlog.user.User.unidentifiedUser;

@Service
class UserCRUDService {

    private final UserRepository userRepository;

    UserCRUDService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    List<UserResponseDTO> findUserDTOs() {
        return userRepository.findUserDTOs();
    }

    Optional<UserResponseDTO> findUserDTOById(long userId) {
        return userRepository.findUserDTOById(userId);
    }

    User createUser(String email, String password, String firstName, String lastName) {
        return userRepository.save(unidentifiedUser(email, password, firstName, lastName));
    }

    User updateUser(long userId, String email, String password, String firstName, String lastName) {
        Optional<User> optionalUserToUpdate = Optional.ofNullable(userRepository.findOne(userId));
        User userToUpdate = optionalUserToUpdate.orElseThrow(() -> new UserNotFoundException(userId));

        userToUpdate.setEmail(email);
        userToUpdate.setPassword(password);
        userToUpdate.setFirstName(firstName);
        userToUpdate.setLastName(lastName);

        try {
            return userRepository.save(userToUpdate);
        } catch (DataIntegrityViolationException constraintViolationException) {
            throw new DuplicateUserException(email);
        }
    }

    void deleteUser(long userId) {
        userRepository.delete(userId);
    }
}
