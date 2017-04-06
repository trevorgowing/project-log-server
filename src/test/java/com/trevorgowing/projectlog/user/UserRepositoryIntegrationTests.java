package com.trevorgowing.projectlog.user;

import com.trevorgowing.projectlog.common.types.AbstractRepositoryIntegrationTests;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

import static com.shazam.shazamcrest.matcher.Matchers.sameBeanAs;
import static com.trevorgowing.projectlog.user.UserBuilder.aUser;
import static com.trevorgowing.projectlog.user.UserResponseDTOBuilder.aUserResponseDTO;
import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;

public class UserRepositoryIntegrationTests extends AbstractRepositoryIntegrationTests {

    private static final String USER_TWO_EMAIL = "usertwo@trevorgowing.com";
    private static final String USER_TWO_PASSWORD = "usertwopassword";
    private static final String USER_TWO_FIRST_NAME = "usertwo";
    private static final String USER_TWO_LAST_NAME = "hibernate";

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testFindUserDTOsWithNoExistingUsers_shouldReturnNoUsers() {
        // Exercise SUT
        List<UserResponseDTO> actualUserResponseDTOS = userRepository.findUserDTOs();

        // Verify results
        assertThat(actualUserResponseDTOS, is(empty()));
    }

    @Test
    public void testFindUserDTOsWithExistingUsers_shouldReturnAllUsers() {
        // Set up fixture
        User userOne = aUser()
                .email(IRRELEVANT_USER_EMAIL)
                .password(IRRELEVANT_USER_PASSWORD)
                .firstName(IRRELEVANT_USER_FIRST_NAME)
                .lastName(IRRELEVANT_USER_LAST_NAME)
                .buildAndPersist(entityManager);

        UserResponseDTO userOneDTO = aUserResponseDTO()
                .id(userOne.getId())
                .email(IRRELEVANT_USER_EMAIL)
                .password(IRRELEVANT_USER_PASSWORD)
                .firstName(IRRELEVANT_USER_FIRST_NAME)
                .lastName(IRRELEVANT_USER_LAST_NAME)
                .build();

        User userTwo = aUser()
                .email(USER_TWO_EMAIL)
                .password(USER_TWO_PASSWORD)
                .firstName(USER_TWO_FIRST_NAME)
                .lastName(USER_TWO_LAST_NAME)
                .buildAndPersist(entityManager);

        UserResponseDTO userTwoDTO = aUserResponseDTO()
                .id(userTwo.getId())
                .email(USER_TWO_EMAIL)
                .password(USER_TWO_PASSWORD)
                .firstName(USER_TWO_FIRST_NAME)
                .lastName(USER_TWO_LAST_NAME)
                .build();

        List<UserResponseDTO> expectedUserResponseDTOS = asList(userOneDTO, userTwoDTO);

        // Exercise SUT
        List<UserResponseDTO> actualUserResponseDTOS = userRepository.findUserDTOs();

        // Verify results
        assertThat(actualUserResponseDTOS, is(expectedUserResponseDTOS));
    }

    @Test
    public void testFindUserDTOByUserIdWithNoExistingUser_shouldEmptyOptional() {
        Optional<UserResponseDTO> optional = userRepository.findUserDTOById(IRRELEVANT_USER_ID);
        assertThat(optional.isPresent(), is(false));
    }

    @Test
    public void testFindUserDTOByIdWithExistingUser_shouldReturnUserMatchingId() {
        // Set up fixture
        User expectedUser = aUser()
                .email(IRRELEVANT_USER_EMAIL)
                .password(IRRELEVANT_USER_PASSWORD)
                .firstName(IRRELEVANT_USER_FIRST_NAME)
                .lastName(IRRELEVANT_USER_LAST_NAME)
                .buildAndPersist(entityManager);

        aUser()
                .email(USER_TWO_EMAIL)
                .password(USER_TWO_PASSWORD)
                .firstName(USER_TWO_FIRST_NAME)
                .lastName(USER_TWO_LAST_NAME)
                .buildAndPersist(entityManager);

        UserResponseDTO expectedUserResponseDTO = aUserResponseDTO()
                .id(expectedUser.getId())
                .email(IRRELEVANT_USER_EMAIL)
                .password(IRRELEVANT_USER_PASSWORD)
                .firstName(IRRELEVANT_USER_FIRST_NAME)
                .lastName(IRRELEVANT_USER_LAST_NAME)
                .build();

        // Exercise SUT
        Optional<UserResponseDTO> actualUserDTOOptional = userRepository.findUserDTOById(expectedUser.getId());

        // Verify results
        assertThat(actualUserDTOOptional.isPresent(), is(true));
        //noinspection OptionalGetWithoutIsPresent
        assertThat(actualUserDTOOptional.get(), sameBeanAs(expectedUserResponseDTO));
    }
}
