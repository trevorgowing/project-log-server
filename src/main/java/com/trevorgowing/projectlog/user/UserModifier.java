package com.trevorgowing.projectlog.user;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.trevorgowing.projectlog.user.DuplicateEmailException.causedDuplicateEmailException;

@Service
public class UserModifier {

    private final UserRetriever userRetriever;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    UserModifier(UserRetriever userRetriever, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRetriever = userRetriever;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    User updateUser(long userId, String email, String password, String firstName, String lastName) {
        User userToUpdate = userRetriever.findUser(userId);

        userToUpdate.setEmail(email);
        userToUpdate.setPassword(passwordEncoder.encode(password));
        userToUpdate.setFirstName(firstName);
        userToUpdate.setLastName(lastName);

        try {
            return userRepository.save(userToUpdate);
        } catch (DataIntegrityViolationException dataIntegrityViolationException) {
            throw causedDuplicateEmailException(email, dataIntegrityViolationException);
        }
    }
}
