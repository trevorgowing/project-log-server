package com.trevorgowing.projectlog.user;

import com.trevorgowing.projectlog.common.types.AbstractTests;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.trevorgowing.projectlog.user.UserBuilder.aUser;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

public class UserCRUDServiceTests extends AbstractTests {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserCRUDService userCRUDService;

    @Test
    public void testFindUserDTOs_shouldDelegateToUserRepositoryToFindUserDTOs() {
        // Set up fixture
        List<UserResponseDTO> expectedUserResponseDTOs = Collections.emptyList();

        // Set up expectations
        when(userRepository.findUserDTOs())
                .thenReturn(expectedUserResponseDTOs);

        // Exercise SUT
        List<UserResponseDTO> actualUserResponseDTOs = userCRUDService.findUserDTOs();

        // Verify behaviour
        assertThat(actualUserResponseDTOs, is(expectedUserResponseDTOs));
    }

    @Test
    public void testFindUserDTOById_shouldDelegateToUserRepositoryToFindUser() {
        // Set up fixture
        Optional<UserResponseDTO> expectedOptionalUserReponseDTO = Optional.empty();

        // Set up expectations
        when(userRepository.findUserDTOById(IRRELEVANT_USER_ID))
                .thenReturn(expectedOptionalUserReponseDTO);

        // Exercise SUT
        Optional<UserResponseDTO> actualOptionalUserResponseDTO = userCRUDService.findUserDTOById(IRRELEVANT_USER_ID);

        // Verify behaviour
        assertThat(actualOptionalUserResponseDTO, is(expectedOptionalUserReponseDTO));
    }

    @Test
    public void testCreateUser_shouldReturnPersistAndReturnNewManagedUser() throws Exception {
        // Set up fixture
        User unidentifiedUser = aUser()
                .email(IRRELEVANT_USER_EMAIL)
                .password(IRRELEVANT_USER_PASSWORD)
                .firstName(IRRELEVANT_USER_FIRST_NAME)
                .lastName(IRRELEVANT_USER_LAST_NAME)
                .build();

        User expectedUser = aUser()
                .id(IRRELEVANT_USER_ID)
                .email(IRRELEVANT_USER_EMAIL)
                .password(IRRELEVANT_USER_PASSWORD)
                .firstName(IRRELEVANT_USER_FIRST_NAME)
                .lastName(IRRELEVANT_USER_LAST_NAME)
                .build();

        // Set up expectations
        when(userRepository.save(unidentifiedUser))
                .thenReturn(expectedUser);

        // Exercise SUT
        User actualUser = userCRUDService.createUser(IRRELEVANT_USER_EMAIL, IRRELEVANT_USER_PASSWORD,
                IRRELEVANT_USER_FIRST_NAME, IRRELEVANT_USER_LAST_NAME);

        // Verify behaviour
        assertThat(actualUser, is(expectedUser));
    }

    @Test
    public void testDeleteUser_shouldDelegateToUserRepositoryToDeleteUser() {
        // Set up expectations
        doNothing().when(userRepository).delete(IRRELEVANT_USER_ID);

        // Exercise SUT
        userCRUDService.deleteUser(IRRELEVANT_USER_ID);
    }
}