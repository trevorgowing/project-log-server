package com.trevorgowing.projectlog.user;

import static com.trevorgowing.projectlog.common.converters.ObjectToJSONConverter.convertToJSON;
import static com.trevorgowing.projectlog.user.DuplicateEmailExceptionBuilder.aDuplicateEmailException;
import static com.trevorgowing.projectlog.user.IdentifiedUserDTOBuilder.anIdentifiedUserDTO;
import static com.trevorgowing.projectlog.user.UnidentifiedUserDTOBuilder.anUnidentifiedUserDTO;
import static com.trevorgowing.projectlog.user.UserBuilder.aUser;
import static com.trevorgowing.projectlog.user.UserNotFoundException.identifiedUserNotFoundException;
import static com.trevorgowing.projectlog.user.UserNotFoundExceptionBuilder.aUserNotFoundException;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import com.trevorgowing.projectlog.common.exception.ExceptionResponse;
import com.trevorgowing.projectlog.common.types.AbstractControllerUnitTests;
import com.trevorgowing.projectlog.user.constant.UserConstants;
import io.restassured.http.ContentType;
import java.util.Collections;
import java.util.List;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;

public class UserControllerUnitTests extends AbstractControllerUnitTests {

  @Mock private UserFactory userFactory;
  @Mock private UserDeleter userDeleter;
  @Mock private UserModifier userModifier;
  @Mock private UserRetriever userRetriever;
  @Mock private UserDTOFactory userDTOFactory;

  @InjectMocks private UserController userController;

  @Override
  protected Object getController() {
    return userController;
  }

  @Test
  public void testGetUsersWithNoExistingUsers_shouldRespondWithStatusOKAndReturnNoUsers() {
    // Set up expectations
    when(userRetriever.findIdentifiedUserDTOs()).thenReturn(Collections.emptyList());

    // Exercise SUT
    given()
        .accept(ContentType.JSON)
        .when()
        .get(UserConstants.USERS_URL_PATH)
        .then()
        .log()
        .all()
        .statusCode(HttpStatus.OK.value())
        .contentType(ContentType.JSON)
        .body(is(convertToJSON(Collections.<IdentifiedUserDTO>emptyList())));

    List<IdentifiedUserDTO> actualUsers = userController.getUsers();

    // Verify behaviour
    assertThat(actualUsers, is(empty()));
  }

  @Test
  public void testGetUsersWithExistingUsers_shouldRespondWithStatusOKAndReturnUsers() {
    // Set up fixture
    IdentifiedUserDTO identifiedUserOneDTO =
        anIdentifiedUserDTO()
            .email(IRRELEVANT_USER_EMAIL)
            .password(IRRELEVANT_USER_PASSWORD)
            .firstName(IRRELEVANT_USER_FIRST_NAME)
            .lastName(IRRELEVANT_USER_LAST_NAME)
            .build();

    IdentifiedUserDTO identifiedUserTwoDTO =
        anIdentifiedUserDTO()
            .email("usertwo@trevorgowing.com")
            .password("usertwopassword")
            .firstName("usertwo")
            .lastName("gowing")
            .build();

    List<IdentifiedUserDTO> expectedIdentifiedUserDTOs =
        asList(identifiedUserOneDTO, identifiedUserTwoDTO);

    // Set up expectations
    when(userRetriever.findIdentifiedUserDTOs()).thenReturn(expectedIdentifiedUserDTOs);

    // Exercise SUT
    given()
        .accept(ContentType.JSON)
        .when()
        .get(UserConstants.USERS_URL_PATH)
        .then()
        .log()
        .all()
        .statusCode(HttpStatus.OK.value())
        .contentType(ContentType.JSON)
        .body(is(convertToJSON(expectedIdentifiedUserDTOs)));

    List<IdentifiedUserDTO> actualIdentifiedUserDTOs = userController.getUsers();

    // Verify behaviour
    assertThat(actualIdentifiedUserDTOs, is(expectedIdentifiedUserDTOs));
  }

  @Test(expected = UserNotFoundException.class)
  public void testGetUserWithNoMatchingUser_shouldRespondWithStatusNotFound() {
    // Set up fixture
    ExceptionResponse exceptionResponse =
        ExceptionResponse.builder()
            .status(NOT_FOUND)
            .message("User not found for id: " + IRRELEVANT_USER_ID)
            .build();

    // Set up expectations
    when(userRetriever.findIdentifiedUserDTOById(IRRELEVANT_USER_ID))
        .thenThrow(identifiedUserNotFoundException(IRRELEVANT_USER_ID));

    // Exercise SUT
    given()
        .accept(ContentType.JSON)
        .when()
        .get(UserConstants.USERS_URL_PATH + "/" + IRRELEVANT_USER_ID)
        .then()
        .log()
        .all()
        .statusCode(NOT_FOUND.value())
        .body(is(equalTo(convertToJSON(exceptionResponse))));

    userController.getUser(IRRELEVANT_USER_ID);
  }

  @Test
  public void testGetUserWithMatchingUser_shouldRespondWithStatusOKAndReturnUser() {
    // Set up fixture
    IdentifiedUserDTO expectedIdentifiedUserDTO =
        anIdentifiedUserDTO()
            .email(IRRELEVANT_USER_EMAIL)
            .password(IRRELEVANT_USER_PASSWORD)
            .firstName(IRRELEVANT_USER_FIRST_NAME)
            .lastName(IRRELEVANT_USER_LAST_NAME)
            .build();

    // Set up expectations
    when(userRetriever.findIdentifiedUserDTOById(IRRELEVANT_USER_ID))
        .thenReturn(expectedIdentifiedUserDTO);

    // Exercise SUT
    given()
        .accept(ContentType.JSON)
        .when()
        .get(UserConstants.USERS_URL_PATH + "/" + IRRELEVANT_USER_ID)
        .then()
        .log()
        .all()
        .statusCode(HttpStatus.OK.value())
        .contentType(ContentType.JSON)
        .body(is(convertToJSON(expectedIdentifiedUserDTO)));

    IdentifiedUserDTO actualIdentifiedUserDTO = userController.getUser(IRRELEVANT_USER_ID);

    // Verify behaviour
    assertThat(actualIdentifiedUserDTO, is(expectedIdentifiedUserDTO));
  }

  @Test(expected = DuplicateEmailException.class)
  public void testPostUserWithDuplicateEmail_shouldRespondWithStatusConflict() {
    // Set up fixture
    UnidentifiedUserDTO unidentifiedUserDTO =
        anUnidentifiedUserDTO()
            .email(IRRELEVANT_USER_EMAIL)
            .password(IRRELEVANT_USER_PASSWORD)
            .firstName(IRRELEVANT_USER_FIRST_NAME)
            .lastName(IRRELEVANT_USER_LAST_NAME)
            .build();

    DuplicateEmailException duplicateEmailException =
        aDuplicateEmailException().withEmail(IRRELEVANT_USER_EMAIL).build();

    ExceptionResponse exceptionResponse =
        ExceptionResponse.builder()
            .status(CONFLICT)
            .message("Duplicate user found with email address: " + IRRELEVANT_USER_EMAIL)
            .build();

    // Set up expectations
    when(userFactory.createUser(
            IRRELEVANT_USER_EMAIL,
            IRRELEVANT_USER_PASSWORD,
            IRRELEVANT_USER_FIRST_NAME,
            IRRELEVANT_USER_LAST_NAME))
        .thenThrow(duplicateEmailException);

    // Exercise SUT
    given()
        .accept(ContentType.JSON)
        .contentType(ContentType.JSON)
        .body(convertToJSON(unidentifiedUserDTO))
        .when()
        .post(UserConstants.USERS_URL_PATH)
        .then()
        .log()
        .all()
        .statusCode(HttpStatus.CONFLICT.value())
        .body(is(equalTo(convertToJSON(exceptionResponse))));

    userController.postUser(unidentifiedUserDTO);
  }

  @Test
  public void testPostUserWithUniqueEmail_shouldRespondWithStatusCreatedAndReturnCreatedUser() {
    // Set up fixture
    UnidentifiedUserDTO unidentifiedUserDTO =
        anUnidentifiedUserDTO()
            .email(IRRELEVANT_USER_EMAIL)
            .password(IRRELEVANT_USER_PASSWORD)
            .firstName(IRRELEVANT_USER_FIRST_NAME)
            .lastName(IRRELEVANT_USER_LAST_NAME)
            .build();

    User user =
        aUser()
            .id(IRRELEVANT_USER_ID)
            .email(IRRELEVANT_USER_EMAIL)
            .password(IRRELEVANT_USER_PASSWORD)
            .firstName(IRRELEVANT_USER_FIRST_NAME)
            .lastName(IRRELEVANT_USER_LAST_NAME)
            .build();

    IdentifiedUserDTO expectedIdentifiedUserDTO =
        anIdentifiedUserDTO()
            .id(IRRELEVANT_USER_ID)
            .email(IRRELEVANT_USER_EMAIL)
            .password(IRRELEVANT_USER_PASSWORD)
            .firstName(IRRELEVANT_USER_FIRST_NAME)
            .lastName(IRRELEVANT_USER_LAST_NAME)
            .build();

    // Set up expectations
    when(userFactory.createUser(
            IRRELEVANT_USER_EMAIL,
            IRRELEVANT_USER_PASSWORD,
            IRRELEVANT_USER_FIRST_NAME,
            IRRELEVANT_USER_LAST_NAME))
        .thenReturn(user);
    when(userDTOFactory.createIdentifiedUserDTO(user)).thenReturn(expectedIdentifiedUserDTO);

    // Exercise SUT
    given()
        .accept(ContentType.JSON)
        .contentType(ContentType.JSON)
        .body(convertToJSON(unidentifiedUserDTO))
        .when()
        .post(UserConstants.USERS_URL_PATH)
        .then()
        .log()
        .all()
        .contentType(ContentType.JSON)
        .statusCode(HttpStatus.CREATED.value())
        .body(is(convertToJSON(expectedIdentifiedUserDTO)));

    IdentifiedUserDTO actualIdentifiedUserDTO = userController.postUser(unidentifiedUserDTO);

    // Verify behaviour
    assertThat(actualIdentifiedUserDTO, is(expectedIdentifiedUserDTO));
  }

  @Test(expected = UserNotFoundException.class)
  public void testPutUserWithNoMatchingUser_shouldRespondWithStatusNotFound() {
    // Set up fixture
    IdentifiedUserDTO identifiedUserDTO =
        anIdentifiedUserDTO()
            .id(IRRELEVANT_USER_ID)
            .email(IRRELEVANT_USER_EMAIL)
            .password(IRRELEVANT_USER_PASSWORD)
            .firstName(IRRELEVANT_USER_FIRST_NAME)
            .lastName(IRRELEVANT_USER_LAST_NAME)
            .build();

    UserNotFoundException userNotFoundException =
        aUserNotFoundException().id(IRRELEVANT_USER_ID).build();

    ExceptionResponse exceptionResponse =
        ExceptionResponse.builder()
            .status(NOT_FOUND)
            .message("User not found for id: " + IRRELEVANT_USER_ID)
            .build();

    // Set up expectations
    when(userModifier.updateUser(
            IRRELEVANT_USER_ID,
            IRRELEVANT_USER_EMAIL,
            IRRELEVANT_USER_PASSWORD,
            IRRELEVANT_USER_FIRST_NAME,
            IRRELEVANT_USER_LAST_NAME))
        .thenThrow(userNotFoundException);

    // Exercise SUT
    given()
        .accept(ContentType.JSON)
        .contentType(ContentType.JSON)
        .body(convertToJSON(identifiedUserDTO))
        .when()
        .put(UserConstants.USERS_URL_PATH + "/" + IRRELEVANT_USER_ID)
        .then()
        .log()
        .all()
        .statusCode(NOT_FOUND.value())
        .body(is(equalTo(convertToJSON(exceptionResponse))));

    userController.putUser(IRRELEVANT_USER_ID, identifiedUserDTO);
  }

  @Test(expected = DuplicateEmailException.class)
  public void testPutUserWithDuplicateEmail_shouldRespondWithStatusConflict() {
    // Set up fixture
    IdentifiedUserDTO identifiedUserDTO =
        anIdentifiedUserDTO()
            .id(IRRELEVANT_USER_ID)
            .email(IRRELEVANT_USER_EMAIL)
            .password(IRRELEVANT_USER_PASSWORD)
            .firstName(IRRELEVANT_USER_FIRST_NAME)
            .lastName(IRRELEVANT_USER_LAST_NAME)
            .build();

    DuplicateEmailException duplicateEmailException =
        aDuplicateEmailException().withEmail(IRRELEVANT_USER_EMAIL).build();

    // Set up expectations
    when(userModifier.updateUser(
            IRRELEVANT_USER_ID,
            IRRELEVANT_USER_EMAIL,
            IRRELEVANT_USER_PASSWORD,
            IRRELEVANT_USER_FIRST_NAME,
            IRRELEVANT_USER_LAST_NAME))
        .thenThrow(duplicateEmailException);

    // Exercise SUT
    given()
        .accept(ContentType.JSON)
        .contentType(ContentType.JSON)
        .body(convertToJSON(identifiedUserDTO))
        .when()
        .put(UserConstants.USERS_URL_PATH + "/" + IRRELEVANT_USER_ID)
        .then()
        .log()
        .all()
        .statusCode(HttpStatus.CONFLICT.value());

    userController.putUser(IRRELEVANT_USER_ID, identifiedUserDTO);
  }

  @Test
  public void testPutUserWithValidUser_shouldRespondWithStatusOKAndReturnTheUpdatedUser() {
    // Set up fixture
    IdentifiedUserDTO identifiedUserDTO =
        anIdentifiedUserDTO()
            .id(IRRELEVANT_USER_ID)
            .email(IRRELEVANT_USER_EMAIL)
            .password(IRRELEVANT_USER_PASSWORD)
            .firstName(IRRELEVANT_USER_FIRST_NAME)
            .lastName(IRRELEVANT_USER_LAST_NAME)
            .build();

    User user =
        aUser()
            .id(IRRELEVANT_USER_ID)
            .email("email")
            .password("password")
            .firstName("first.name")
            .lastName("last.name")
            .build();

    IdentifiedUserDTO expectedIdentifiedUserDTO =
        anIdentifiedUserDTO()
            .id(IRRELEVANT_USER_ID)
            .email(IRRELEVANT_USER_EMAIL)
            .password(IRRELEVANT_USER_PASSWORD)
            .firstName(IRRELEVANT_USER_FIRST_NAME)
            .lastName(IRRELEVANT_USER_LAST_NAME)
            .build();

    // Set up expectations
    when(userModifier.updateUser(
            IRRELEVANT_USER_ID,
            IRRELEVANT_USER_EMAIL,
            IRRELEVANT_USER_PASSWORD,
            IRRELEVANT_USER_FIRST_NAME,
            IRRELEVANT_USER_LAST_NAME))
        .thenReturn(user);
    when(userDTOFactory.createIdentifiedUserDTO(user)).thenReturn(expectedIdentifiedUserDTO);

    // Exercise SUT
    given()
        .accept(ContentType.JSON)
        .contentType(ContentType.JSON)
        .body(convertToJSON(identifiedUserDTO))
        .when()
        .put(UserConstants.USERS_URL_PATH + "/" + IRRELEVANT_USER_ID)
        .then()
        .log()
        .all()
        .contentType(ContentType.JSON)
        .statusCode(HttpStatus.OK.value())
        .body(is(convertToJSON(expectedIdentifiedUserDTO)));

    IdentifiedUserDTO actualIdentifiedUserDTO =
        userController.putUser(IRRELEVANT_USER_ID, identifiedUserDTO);

    // Verify behaviour
    assertThat(actualIdentifiedUserDTO, is(expectedIdentifiedUserDTO));
  }

  @Test(expected = UserNotFoundException.class)
  public void testDeleteUserWithNoMatchingUser_shouldRespondWithStatusNotFound() {
    // Set up fixture
    ExceptionResponse exceptionResponse =
        ExceptionResponse.builder()
            .status(NOT_FOUND)
            .message("User not found for id: " + IRRELEVANT_USER_ID)
            .build();

    // Set up expectations
    doThrow(identifiedUserNotFoundException(IRRELEVANT_USER_ID))
        .when(userDeleter)
        .deleteUser(IRRELEVANT_USER_ID);

    // Exercise SUT
    given()
        .when()
        .delete(UserConstants.USERS_URL_PATH + "/" + IRRELEVANT_USER_ID)
        .then()
        .log()
        .all()
        .statusCode(NOT_FOUND.value())
        .body(is(equalTo(convertToJSON(exceptionResponse))));

    userController.deleteUser(IRRELEVANT_USER_ID);
  }

  @Test
  public void testDeleteUserWithMatchingUser_shouldRespondWithStatusNoContent() {
    // Set up expectations
    doNothing().when(userDeleter).deleteUser(IRRELEVANT_USER_ID);

    // Exercise SUT
    given()
        .when()
        .delete(UserConstants.USERS_URL_PATH + "/" + IRRELEVANT_USER_ID)
        .then()
        .log()
        .all()
        .statusCode(HttpStatus.NO_CONTENT.value());

    userController.deleteUser(IRRELEVANT_USER_ID);
  }
}
