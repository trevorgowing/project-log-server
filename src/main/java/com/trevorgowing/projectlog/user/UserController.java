package com.trevorgowing.projectlog.user;

import com.trevorgowing.projectlog.user.constant.UserConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(UserConstants.USERS_URL_PATH)
class UserController {

    private final UserRepository userRepository;

    UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    List<UserDTO> getUsers() {
        return userRepository.findUserDTOs();
    }

    @GetMapping(path = UserConstants.USER_ID_PARAMETER_URL_PATH)
    @ResponseStatus(HttpStatus.OK)
    UserDTO getUser(@PathVariable long userId) {
        return userRepository.findUserDTOById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(code = HttpStatus.NOT_FOUND, reason = UserNotFoundException.REASON)
    public void handleUserNotFoundException(UserNotFoundException userNotFoundException) {
        log.warn(userNotFoundException.getMessage(), userNotFoundException);
    }
}