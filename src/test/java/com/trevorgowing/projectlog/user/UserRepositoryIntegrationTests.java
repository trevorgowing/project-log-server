package com.trevorgowing.projectlog.user;

import com.trevorgowing.projectlog.common.types.AbstractRepositoryIntegrationTests;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;

public class UserRepositoryIntegrationTests extends AbstractRepositoryIntegrationTests {

    private static final String USER_ONE_EMAIL = "userone@trevorgowing.com";
    private static final String USER_ONE_PASSWORD = "useronepassword";
    private static final String USER_ONE_FIRST_NAME = "userone";
    private static final String USER_ONE_LAST_NAME = "spring";

    private static final String USER_TWO_EMAIL = "usertwo@trevorgowing.com";
    private static final String USER_TWO_PASSWORD = "usertwopassword";
    private static final String USER_TWO_FIRST_NAME = "usertwo";
    private static final String USER_TWO_LAST_NAME = "hibernate";

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testFindUserDTOsWithNoExistingUsers_shouldAllUsers() {
        // Exercise SUT
        List<UserDTO> actualUserDTOs = userRepository.findUserDTOs();

        // Verify results
        assertThat(actualUserDTOs, is(empty()));
    }

    @Test
    public void testFindUserDTOsWithExistingUsers_shouldAllUsers() {
        // Set up fixture
        entityManager.merge(User.builder()
                .email(USER_ONE_EMAIL)
                .password(USER_ONE_PASSWORD)
                .firstName(USER_ONE_FIRST_NAME)
                .lastName(USER_ONE_LAST_NAME)
                .build());

        UserDTO userOneDTO = UserDTO.builder()
                .email(USER_ONE_EMAIL)
                .password(USER_ONE_PASSWORD)
                .firstName(USER_ONE_FIRST_NAME)
                .lastName(USER_ONE_LAST_NAME)
                .build();

        entityManager.merge(User.builder()
                .email(USER_TWO_EMAIL)
                .password(USER_TWO_PASSWORD)
                .firstName(USER_TWO_FIRST_NAME)
                .lastName(USER_TWO_LAST_NAME)
                .build());

        UserDTO userTwoDTO = UserDTO.builder()
                .email(USER_TWO_EMAIL)
                .password(USER_TWO_PASSWORD)
                .firstName(USER_TWO_FIRST_NAME)
                .lastName(USER_TWO_LAST_NAME)
                .build();

        List<UserDTO> expectedUserDTOs = asList(userOneDTO, userTwoDTO);

        // Exercise SUT
        List<UserDTO> actualUserDTOs = userRepository.findUserDTOs();

        // Verify results
        assertThat(actualUserDTOs, is(expectedUserDTOs));
    }
}
