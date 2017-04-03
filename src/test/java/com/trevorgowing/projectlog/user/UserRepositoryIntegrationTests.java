package com.trevorgowing.projectlog.user;

import com.trevorgowing.projectlog.common.types.AbstractRepositoryIntegrationTests;
import com.trevorgowing.projectlog.common.types.AbstractTests;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

import static com.shazam.shazamcrest.matcher.Matchers.sameBeanAs;
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
        List<UserDTO> actualUserDTOs = userRepository.findUserDTOs();

        // Verify results
        assertThat(actualUserDTOs, is(empty()));
    }

    @Test
    public void testFindUserDTOsWithExistingUsers_shouldReturnAllUsers() {
        // Set up fixture
        User userOne = User.builder()
                .email(IRRELEVANT_USER_EMAIL)
                .password(IRRELEVANT_USER_PASSWORD)
                .firstName(IRRELEVANT_USER_FIRST_NAME)
                .lastName(IRRELEVANT_USER_LAST_NAME)
                .build();
        userOne = entityManager.merge(userOne);

        UserDTO userOneDTO = UserDTO.builder()
                .id(userOne.getId())
                .email(IRRELEVANT_USER_EMAIL)
                .password(IRRELEVANT_USER_PASSWORD)
                .firstName(IRRELEVANT_USER_FIRST_NAME)
                .lastName(IRRELEVANT_USER_LAST_NAME)
                .build();

        User userTwo = User.builder()
                .email(USER_TWO_EMAIL)
                .password(USER_TWO_PASSWORD)
                .firstName(USER_TWO_FIRST_NAME)
                .lastName(USER_TWO_LAST_NAME)
                .build();
        userTwo = entityManager.merge(userTwo);

        UserDTO userTwoDTO = UserDTO.builder()
                .id(userTwo.getId())
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

    @Test
    public void testFindUserDTOByUserIdWithNoExistingUser_shouldEmptyOptional() {
        Optional<UserDTO> optional = userRepository.findUserDTOById(IRRELEVANT_USER_ID);
        assertThat(optional.isPresent(), is(false));
    }

    @Test
    public void testFindUserDTOByIdWithExistingUser_shouldReturnUserMatchingId() {
        // Set up fixture
        User expectedUser = User.builder()
                .email(IRRELEVANT_USER_EMAIL)
                .password(IRRELEVANT_USER_PASSWORD)
                .firstName(AbstractTests.IRRELEVANT_USER_FIRST_NAME)
                .lastName(IRRELEVANT_USER_LAST_NAME)
                .build();
        expectedUser = entityManager.merge(expectedUser);

        entityManager.merge(User.builder()
                .email(USER_TWO_EMAIL)
                .password(USER_TWO_PASSWORD)
                .firstName(USER_TWO_FIRST_NAME)
                .lastName(USER_TWO_LAST_NAME)
                .build());

        UserDTO expectedUserDTO = UserDTO.builder()
                .id(expectedUser.getId())
                .email(IRRELEVANT_USER_EMAIL)
                .password(IRRELEVANT_USER_PASSWORD)
                .firstName(AbstractTests.IRRELEVANT_USER_FIRST_NAME)
                .lastName(IRRELEVANT_USER_LAST_NAME)
                .build();

        // Exercise SUT
        Optional<UserDTO> actualUserDTOOptional = userRepository.findUserDTOById(expectedUser.getId());

        // Verify results
        assertThat(actualUserDTOOptional.isPresent(), is(true));
        //noinspection OptionalGetWithoutIsPresent
        assertThat(actualUserDTOOptional.get(), sameBeanAs(expectedUserDTO));
    }
}
