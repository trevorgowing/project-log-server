package com.trevorgowing.projectlog.user;

import com.trevorgowing.projectlog.common.types.AbstractTests;
import org.junit.Test;

import static com.shazam.shazamcrest.matcher.Matchers.sameBeanAs;
import static com.trevorgowing.projectlog.user.UserBuilder.aUser;
import static com.trevorgowing.projectlog.user.UserResponseDTOBuilder.aUserResponseDTO;
import static org.hamcrest.MatcherAssert.assertThat;

public class UserDTOFactoryTests extends AbstractTests {

    private final UserDTOFactory userDTOFactory = new UserDTOFactory();

    @Test
    public void testCreateUserResponseDTO_shouldReturnNewUserResponseDTO() throws Exception {
        // Set up fixture
        User user = aUser()
                .id(IRRELEVANT_USER_ID)
                .email(IRRELEVANT_USER_EMAIL)
                .password(IRRELEVANT_USER_PASSWORD)
                .firstName(IRRELEVANT_USER_FIRST_NAME)
                .lastName(IRRELEVANT_USER_LAST_NAME)
                .build();

        UserResponseDTO expectedUserResponseDTO = aUserResponseDTO()
                .id(IRRELEVANT_USER_ID)
                .email(IRRELEVANT_USER_EMAIL)
                .password(IRRELEVANT_USER_PASSWORD)
                .firstName(IRRELEVANT_USER_FIRST_NAME)
                .lastName(IRRELEVANT_USER_LAST_NAME)
                .build();

        // Exercise SUT
        UserResponseDTO actualUserResponseDTO = userDTOFactory.createUserResponseDTO(user);

        // Verify behaviour and state
        assertThat(actualUserResponseDTO, sameBeanAs(expectedUserResponseDTO));
    }
}