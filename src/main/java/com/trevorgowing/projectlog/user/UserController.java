package com.trevorgowing.projectlog.user;

import com.trevorgowing.projectlog.user.constant.UserConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(UserConstants.USERS_URL_PATH)
class UserController {

    private final UserDTOFactory userDTOFactory;
    private final UserCRUDService userCRUDService;

    UserController(UserDTOFactory userDTOFactory, UserCRUDService userCRUDService) {
        this.userDTOFactory = userDTOFactory;
        this.userCRUDService = userCRUDService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.OK)
    List<IdentifiedUserDTO> getUsers() {
        return userCRUDService.findIdentifiedUserDTOs();
    }

    @GetMapping(path = UserConstants.USER_ID_PARAMETER_URL_PATH, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.OK)
    IdentifiedUserDTO getUser(@PathVariable long userId) {
        return userCRUDService.findIdentifiedUserDTOById(userId);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    IdentifiedUserDTO postUser(@RequestBody UnidentifiedUserDTO unidentifiedUserDTO) {
        User user = userCRUDService.createUser(unidentifiedUserDTO.getEmail(), unidentifiedUserDTO.getPassword(),
                unidentifiedUserDTO.getFirstName(), unidentifiedUserDTO.getLastName());
        return userDTOFactory.createIdentifiedUserDTO(user);
    }

    @PutMapping(path = UserConstants.USER_ID_PARAMETER_URL_PATH, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.OK)
    IdentifiedUserDTO putUser(@PathVariable long userId, @RequestBody IdentifiedUserDTO identifiedUserDTO) {
        User updatedUser = userCRUDService.updateUser(userId, identifiedUserDTO.getEmail(),
                identifiedUserDTO.getPassword(), identifiedUserDTO.getFirstName(), identifiedUserDTO.getLastName());
        return userDTOFactory.createIdentifiedUserDTO(updatedUser);
    }

    @DeleteMapping(path = UserConstants.USER_ID_PARAMETER_URL_PATH)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteUser(@PathVariable long userId) {
        userCRUDService.deleteUser(userId);
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(code = HttpStatus.NOT_FOUND, reason = UserNotFoundException.REASON)
    public void handleUserNotFoundException(UserNotFoundException userNotFoundException) {
        log.warn(userNotFoundException.getMessage(), userNotFoundException);
    }

    @ExceptionHandler(DuplicateEmailException.class)
    @ResponseStatus(code = HttpStatus.CONFLICT, reason = DuplicateEmailException.REASON)
    public void handleDuplicateEmailException(DuplicateEmailException duplicateEmailException) {
        log.warn(duplicateEmailException.getMessage(), duplicateEmailException);
    }
}