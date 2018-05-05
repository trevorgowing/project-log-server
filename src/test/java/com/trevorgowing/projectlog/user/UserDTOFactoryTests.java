package com.trevorgowing.projectlog.user;

import static com.trevorgowing.projectlog.user.IdentifiedUserDTOBuilder.anIdentifiedUserDTO;
import static com.trevorgowing.projectlog.user.UserBuilder.aUser;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.samePropertyValuesAs;

import com.trevorgowing.projectlog.common.types.AbstractTests;
import org.junit.Test;

public class UserDTOFactoryTests extends AbstractTests {

  private final UserDTOFactory userDTOFactory = new UserDTOFactory();

  @Test
  public void testCreateIdentifiedUserDTO_shouldCreateIdentifiedUserDTOFromGivenUser()
      throws Exception {
    // Set up fixture
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

    // Exercise SUT
    IdentifiedUserDTO actualIdentifiedUserDTO = userDTOFactory.createIdentifiedUserDTO(user);

    // Verify behaviour and state
    assertThat(actualIdentifiedUserDTO, samePropertyValuesAs(expectedIdentifiedUserDTO));
  }
}
