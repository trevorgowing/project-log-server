package com.trevorgowing.projectlog.user;

import org.springframework.stereotype.Service;

import static com.trevorgowing.projectlog.user.User.unidentifiedUser;

@Service
class UserCRUDService {

    private final UserRepository userRepository;

    UserCRUDService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    User createUser(String email, String password, String firstName, String lastName) {
        return userRepository.save(unidentifiedUser(email, password, firstName, lastName));
    }

    void deleteUser(long userId) {
        userRepository.delete(userId);
    }
}
