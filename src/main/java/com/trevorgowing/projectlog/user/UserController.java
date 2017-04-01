package com.trevorgowing.projectlog.user;

import com.trevorgowing.projectlog.user.constant.UserConstants;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(UserConstants.USERS_URL_PATH)
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    public List<UserDTO> getUsers() {
        return userRepository.findUserDTOs();
    }
}
