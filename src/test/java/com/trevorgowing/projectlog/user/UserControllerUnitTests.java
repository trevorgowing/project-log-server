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

import static com.shazam.shazamcrest.matcher.Matchers.sameBeanAs;
import static com.trevorgowing.projectlog.common.converters.ObjectToJSON.convertToJSON;
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
                .body(sameBeanAs(convertToJSON(Collections.<UserDTO>emptyList())));

        List<UserDTO> actualUsers = userController.getUsers();

        // Verify behaviour
        assertThat(actualUsers, is(empty()));
    }

    @Test
    public void testGetUsersWithExistingUsers_shouldDelegateToUserRepositoryAndReturnUsers() throws Exception {
        // Set up fixture
        UserDTO userOne = UserDTO.builder()
                .email("userone@trevorgowing.com")
                .password("useronepassword")
                .firstName("userone")
                .lastName("gowing")
                .build();

        UserDTO userTwo = UserDTO.builder()
                .email("usertwo@trevorgowing.com")
                .password("usertwopassword")
                .firstName("usertwo")
                .lastName("gowing")
                .build();

        List<UserDTO> expectedUsers = asList(userOne, userTwo);

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

        List<UserDTO> actualUsers = userController.getUsers();

        // Verify behaviour
        assertThat(actualUsers, is(expectedUsers));
    }
}
