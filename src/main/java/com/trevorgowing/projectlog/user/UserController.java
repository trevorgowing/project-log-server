package com.trevorgowing.projectlog.user;

import com.trevorgowing.projectlog.user.constant.UserConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(UserConstants.USERS_URL_PATH)
class UserController {

    private final UserDTOFactory userDTOFactory;
    private final UserCRUDService userCRUDService;
    private final UserRepository userRepository;

    UserController(UserDTOFactory userDTOFactory, UserCRUDService userCRUDService, UserRepository userRepository) {
        this.userDTOFactory = userDTOFactory;
        this.userCRUDService = userCRUDService;
        this.userRepository = userRepository;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.OK)
    List<UserResponseDTO> getUsers() {
        return userRepository.findUserDTOs();
    }

    @GetMapping(path = UserConstants.USER_ID_PARAMETER_URL_PATH, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.OK)
    UserResponseDTO getUser(@PathVariable long userId) {
        return userRepository.findUserDTOById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(code = HttpStatus.NOT_FOUND, reason = UserNotFoundException.REASON)
    public void handleUserNotFoundException(UserNotFoundException userNotFoundException) {
        log.warn(userNotFoundException.getMessage(), userNotFoundException);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    UserResponseDTO postUser(@RequestBody UserRequestDTO userRequestDTO) {
        User user = userCRUDService.createUser(userRequestDTO.getEmail(), userRequestDTO.getPassword(),
                userRequestDTO.getFirstName(), userRequestDTO.getLastName());
        return userDTOFactory.createUserResponseDTO(user);
    }

    @ExceptionHandler(DuplicateUserException.class)
    @ResponseStatus(code = HttpStatus.CONFLICT, reason = DuplicateUserException.REASON)
    public void handleDuplicateUserException(DuplicateUserException duplicateUserException) {
        log.warn(duplicateUserException.getMessage(), duplicateUserException);
    }

    @DeleteMapping(path = UserConstants.USER_ID_PARAMETER_URL_PATH)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteUser(@PathVariable long userId) {
        userCRUDService.deleteUser(userId);
    }
}