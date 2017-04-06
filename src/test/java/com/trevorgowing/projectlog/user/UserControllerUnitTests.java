package com.trevorgowing.projectlog.user;

import com.trevorgowing.projectlog.common.types.AbstractControllerUnitTests;
import com.trevorgowing.projectlog.user.constant.UserConstants;
import io.restassured.http.ContentType;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.shazam.shazamcrest.matcher.Matchers.sameBeanAs;
import static com.trevorgowing.projectlog.common.converters.ObjectToJSON.convertToJSON;
import static com.trevorgowing.projectlog.user.UserResponseDTOBuilder.aUserResponseDTO;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

public class UserControllerUnitTests extends AbstractControllerUnitTests {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserController userController;

    @Override
    protected Object getController() {
        return userController;
    }

    @Test
    public void testGetUsersWithNoExistingUsers_shouldDelegateToUserRepositoryAndReturnEmptyCollection() throws Exception {
        // Set up expectations
        when(userRepository.findUserDTOs()).thenReturn(Collections.emptyList());

        // Exercise SUT
        given()
                .contentType(ContentType.JSON)
        .when()
                .get(UserConstants.USERS_URL_PATH)
        .then()
                .log().all()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON)
                .body(sameBeanAs(convertToJSON(Collections.<UserResponseDTO>emptyList())));

        List<UserResponseDTO> actualUsers = userController.getUsers();

        // Verify behaviour
        assertThat(actualUsers, is(empty()));
    }

    @Test
    public void testGetUsersWithExistingUsers_shouldDelegateToUserRepositoryAndReturnUsers() throws Exception {
        // Set up fixture
        UserResponseDTO userOne = aUserResponseDTO()
                .email(IRRELEVANT_USER_EMAIL)
                .password(IRRELEVANT_USER_PASSWORD)
                .firstName(IRRELEVANT_USER_FIRST_NAME)
                .lastName(IRRELEVANT_USER_LAST_NAME)
                .build();

        UserResponseDTO userTwo = aUserResponseDTO()
                .email("usertwo@trevorgowing.com")
                .password("usertwopassword")
                .firstName("usertwo")
                .lastName("gowing")
                .build();

        List<UserResponseDTO> expectedUsers = asList(userOne, userTwo);

        // Set up expectations
        when(userRepository.findUserDTOs()).thenReturn(expectedUsers);

        // Exercise SUT
        given()
                .contentType(ContentType.JSON)
        .when()
                .get(UserConstants.USERS_URL_PATH)
        .then()
                .log().all()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON)
                .body(sameBeanAs(convertToJSON(expectedUsers)));

        List<UserResponseDTO> actualUsers = userController.getUsers();

        // Verify behaviour
        assertThat(actualUsers, is(expectedUsers));
    }

    @Test(expected = UserNotFoundException.class)
    public void testGetUserWithNoExistingUser_shouldThrowUserNotFoundException() {
        // Set up expectations
        when(userRepository.findUserDTOById(IRRELEVANT_USER_ID))
                .thenReturn(Optional.empty());

        // Exercise SUT
        given()
                .contentType(ContentType.JSON)
        .when()
                .get(UserConstants.USERS_URL_PATH + "/" + IRRELEVANT_USER_ID)
        .then()
                .log().all()
                .statusCode(HttpStatus.NOT_FOUND.value());

        // Exercise SUT
        userController.getUser(IRRELEVANT_USER_ID);
    }

    @Test
    public void testGetUserWithExistingUser_shouldReturnUser() throws Exception {
        // Set up fixture
        UserResponseDTO expectedUser = aUserResponseDTO()
                .email(IRRELEVANT_USER_EMAIL)
                .password(IRRELEVANT_USER_PASSWORD)
                .firstName(IRRELEVANT_USER_FIRST_NAME)
                .lastName(IRRELEVANT_USER_LAST_NAME)
                .build();

        // Set up expectations
        when(userRepository.findUserDTOById(IRRELEVANT_USER_ID))
                .thenReturn(Optional.of(expectedUser));

        // Exercise SUT
        given()
                .contentType(ContentType.JSON)
        .when()
                .get(UserConstants.USERS_URL_PATH + "/" + IRRELEVANT_USER_ID)
        .then()
                .log().all()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON)
                .body(sameBeanAs(convertToJSON(expectedUser)));

        UserResponseDTO actualUserResponseDTO = userController.getUser(IRRELEVANT_USER_ID);

        // Verify behaviour
        assertThat(actualUserResponseDTO, sameBeanAs(expectedUser));
    }
}
