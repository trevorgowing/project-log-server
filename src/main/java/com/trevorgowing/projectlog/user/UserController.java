package com.trevorgowing.projectlog.user;

import static com.trevorgowing.projectlog.user.constant.UserConstants.USERS_URL_PATH;
import static com.trevorgowing.projectlog.user.constant.UserConstants.USER_ID_PARAMETER_URL_PATH;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

import com.trevorgowing.projectlog.common.exception.ExceptionResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
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

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(USERS_URL_PATH)
class UserController {

  private final UserFactory userFactory;
  private final UserDeleter userDeleter;
  private final UserModifier userModifier;
  private final UserRetriever userRetriever;
  private final UserDTOFactory userDTOFactory;

  @GetMapping(produces = APPLICATION_JSON_UTF8_VALUE)
  @ResponseStatus(OK)
  List<IdentifiedUserDTO> getUsers() {
    return userRetriever.findIdentifiedUserDTOs();
  }

  @GetMapping(path = USER_ID_PARAMETER_URL_PATH, produces = APPLICATION_JSON_UTF8_VALUE)
  @ResponseStatus(OK)
  IdentifiedUserDTO getUser(@PathVariable long userId) {
    return userRetriever.findIdentifiedUserDTOById(userId);
  }

  @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE, produces = APPLICATION_JSON_UTF8_VALUE)
  @ResponseStatus(CREATED)
  IdentifiedUserDTO postUser(@RequestBody UnidentifiedUserDTO unidentifiedUserDTO) {
    User user =
        userFactory.createUser(
            unidentifiedUserDTO.getEmail(),
            unidentifiedUserDTO.getPassword(),
            unidentifiedUserDTO.getFirstName(),
            unidentifiedUserDTO.getLastName());
    return userDTOFactory.createIdentifiedUserDTO(user);
  }

  @PutMapping(
    path = USER_ID_PARAMETER_URL_PATH,
    consumes = APPLICATION_JSON_UTF8_VALUE,
    produces = APPLICATION_JSON_UTF8_VALUE
  )
  @ResponseStatus(OK)
  IdentifiedUserDTO putUser(
      @PathVariable long userId, @RequestBody IdentifiedUserDTO identifiedUserDTO) {
    User updatedUser =
        userModifier.updateUser(
            userId,
            identifiedUserDTO.getEmail(),
            identifiedUserDTO.getPassword(),
            identifiedUserDTO.getFirstName(),
            identifiedUserDTO.getLastName());
    return userDTOFactory.createIdentifiedUserDTO(updatedUser);
  }

  @DeleteMapping(path = USER_ID_PARAMETER_URL_PATH)
  @ResponseStatus(NO_CONTENT)
  void deleteUser(@PathVariable long userId) {
    userDeleter.deleteUser(userId);
  }

  @ExceptionHandler(UserNotFoundException.class)
  public ResponseEntity<ExceptionResponse> handleUserNotFoundException(
      UserNotFoundException userNotFoundException) {
    log.debug(userNotFoundException.getMessage(), userNotFoundException);
    return ResponseEntity.status(NOT_FOUND)
        .contentType(APPLICATION_JSON_UTF8)
        .body(ExceptionResponse.from(NOT_FOUND, userNotFoundException.getMessage()));
  }

  @ExceptionHandler(DuplicateEmailException.class)
  public ResponseEntity<ExceptionResponse> handleDuplicateEmailException(
      DuplicateEmailException duplicateEmailException) {
    log.debug(duplicateEmailException.getMessage(), duplicateEmailException);
    return ResponseEntity.status(CONFLICT)
        .contentType(APPLICATION_JSON_UTF8)
        .body(ExceptionResponse.from(CONFLICT, duplicateEmailException.getMessage()));
  }
}
