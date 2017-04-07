package com.trevorgowing.projectlog.user;

import com.trevorgowing.projectlog.common.types.AbstractRepositoryIntegrationTests;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.shazam.shazamcrest.matcher.Matchers.sameBeanAs;
import static com.trevorgowing.projectlog.user.IdentifiedUserDTOBuilder.anIdentifiedUserDTO;
import static com.trevorgowing.projectlog.user.UserBuilder.aUser;
import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class UserRepositoryIntegrationTests extends AbstractRepositoryIntegrationTests {

    private static final String USER_TWO_EMAIL = "usertwo@trevorgowing.com";
    private static final String USER_TWO_PASSWORD = "usertwopassword";
    private static final String USER_TWO_FIRST_NAME = "usertwo";
    private static final String USER_TWO_LAST_NAME = "hibernate";

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testFindIdentifiedUserDTOsWithNoExistingUsers_shouldReturnNoIdentifiedUserDTOs() {
        // Exercise SUT
        List<IdentifiedUserDTO> actualIdentifiedUserDTOS = userRepository.findIdentifiedUserDTOs();

        // Verify results
        assertThat(actualIdentifiedUserDTOS, is(empty()));
    }

    @Test
    public void testFindIdentifiedUserDTOsWithExistingUsers_shouldReturnIdentifiedUserDTOs() {
        // Set up fixture
        User userOne = aUser()
                .email(IRRELEVANT_USER_EMAIL)
                .password(IRRELEVANT_USER_PASSWORD)
                .firstName(IRRELEVANT_USER_FIRST_NAME)
                .lastName(IRRELEVANT_USER_LAST_NAME)
                .buildAndPersist(entityManager);

        IdentifiedUserDTO identifiedUserOneDTO = anIdentifiedUserDTO()
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

        IdentifiedUserDTO identifiedUserTwoDTO = anIdentifiedUserDTO()
                .id(userTwo.getId())
                .email(USER_TWO_EMAIL)
                .password(USER_TWO_PASSWORD)
                .firstName(USER_TWO_FIRST_NAME)
                .lastName(USER_TWO_LAST_NAME)
                .build();

        List<IdentifiedUserDTO> expectedIdentifiedUserDTOS = asList(identifiedUserOneDTO, identifiedUserTwoDTO);

        // Exercise SUT
        List<IdentifiedUserDTO> actualIdentifiedUserDTOS = userRepository.findIdentifiedUserDTOs();

        // Verify results
        assertThat(actualIdentifiedUserDTOS, is(expectedIdentifiedUserDTOS));
    }

    @Test
    public void testFindIdentifiedUserDTOByUserIdWithNoMatchingUser_shouldReturnNull() {
        IdentifiedUserDTO identifiedUserDTO = userRepository.findIdentifiedUserDTOById(IRRELEVANT_USER_ID);
        assertThat(identifiedUserDTO, is(nullValue(IdentifiedUserDTO.class)));
    }

    @Test
    public void testFindUserDTOByIdWithMatchingUser_shouldReturnMatchingUser() {
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

        IdentifiedUserDTO expectedIdentifiedUserDTO = anIdentifiedUserDTO()
                .id(expectedUser.getId())
                .email(IRRELEVANT_USER_EMAIL)
                .password(IRRELEVANT_USER_PASSWORD)
                .firstName(IRRELEVANT_USER_FIRST_NAME)
                .lastName(IRRELEVANT_USER_LAST_NAME)
                .build();

        // Exercise SUT
        IdentifiedUserDTO actualIdentifiedUserDTO = userRepository.findIdentifiedUserDTOById(expectedUser.getId());

        // Verify results
        assertThat(actualIdentifiedUserDTO, sameBeanAs(expectedIdentifiedUserDTO));
    }
}
