package com.trevorgowing.projectlog.user;

import com.trevorgowing.projectlog.common.types.AbstractTests;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.Collections;
import java.util.List;

import static com.trevorgowing.projectlog.user.IdentifiedUserDTOBuilder.anIdentifiedUserDTO;
import static com.trevorgowing.projectlog.user.UserBuilder.aUser;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

public class UserCRUDServiceTests extends AbstractTests {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserCRUDService userCRUDService;

    @Test(expected = UserNotFoundException.class)
    public void testFindUserWithNoMatchingUser_shouldThrowUserNotFoundException() {
        // Set up expectations
        when(userRepository.findOne(IRRELEVANT_USER_ID))
                .thenReturn(null);

        // Exercise SUT
        userCRUDService.findUser(IRRELEVANT_USER_ID);
    }

    @Test
    public void testFindUserWithMatchingUser_shouldReturnUser() {
        // Set up fixture
        User expectedUser = aUser()
                .id(IRRELEVANT_USER_ID)
                .build();

        // Set up expectations
        when(userRepository.findOne(IRRELEVANT_USER_ID))
                .thenReturn(expectedUser);

        // Exercise SUT
        User acutalUser = userCRUDService.findUser(IRRELEVANT_USER_ID);

        // Verify behaviour
        assertThat(acutalUser, is(expectedUser));
    }

    @Test
    public void testFindIdentifiedUserDTOs_shouldDelegateToUserRepositoryAndReturnIdentifiedUserDTOs() {
        // Set up fixture
        List<IdentifiedUserDTO> expectedIdentifiedUserDTOs = Collections.emptyList();

        // Set up expectations
        when(userRepository.findIdentifiedUserDTOs())
                .thenReturn(expectedIdentifiedUserDTOs);

        // Exercise SUT
        List<IdentifiedUserDTO> actualIdentifiedUserDTOs = userCRUDService.findIdentifiedUserDTOs();

        // Verify behaviour
        assertThat(actualIdentifiedUserDTOs, is(expectedIdentifiedUserDTOs));
    }

    @Test(expected = UserNotFoundException.class)
    public void testFindIdentifiedUserDTOByIdWithNoMatchingUser_shouldDelegateToUserRepositoryAndThrowUserNotFoundException() {
        // Set up expectations
        when(userRepository.findIdentifiedUserDTOById(IRRELEVANT_USER_ID))
                .thenReturn(null);

        // Exercise SUT
        userCRUDService.findIdentifiedUserDTOById(IRRELEVANT_USER_ID);
    }

    @Test
    public void testFindIdentifiedUserDTOByIdWithMatchingUser_shouldDelegateToUserRepositoryAndReturnIdentifiedUserDTO() {
        // Set up fixture
        IdentifiedUserDTO expectedIdentifiedUserDTO = anIdentifiedUserDTO()
                .id(IRRELEVANT_USER_ID)
                .email(IRRELEVANT_USER_EMAIL)
                .password(IRRELEVANT_USER_PASSWORD)
                .firstName(IRRELEVANT_USER_FIRST_NAME)
                .lastName(IRRELEVANT_USER_LAST_NAME)
                .build();

        // Set up expectations
        when(userRepository.findIdentifiedUserDTOById(IRRELEVANT_USER_ID)).thenReturn(expectedIdentifiedUserDTO);

        //Exercise SUT
        IdentifiedUserDTO actualIdentifiedUserDTO = userCRUDService.findIdentifiedUserDTOById(IRRELEVANT_USER_ID);

        // Verify behaviour
        assertThat(actualIdentifiedUserDTO, is(expectedIdentifiedUserDTO));
    }

    @Test(expected = DuplicateEmailException.class)
    public void testCreateUserWithDuplicateEmail_shouldDelegateToUserRepositoryAndThrowDuplicateEmailException()
            throws Exception {
        // Set up fixture
        User unidentifiedUser = aUser()
                .email(IRRELEVANT_USER_EMAIL)
                .password(IRRELEVANT_USER_PASSWORD)
                .firstName(IRRELEVANT_USER_FIRST_NAME)
                .lastName(IRRELEVANT_USER_LAST_NAME)
                .build();

        // Set up expectations
        when(userRepository.save(unidentifiedUser))
                .thenThrow(new DataIntegrityViolationException(IRRELEVANT_MESSAGE));

        // Exercise SUT
        userCRUDService.createUser(IRRELEVANT_USER_EMAIL, IRRELEVANT_USER_PASSWORD,
                IRRELEVANT_USER_FIRST_NAME, IRRELEVANT_USER_LAST_NAME);
    }

    @Test
    public void testCreateUserWithValidUser_shouldDelegateToUserRepositoryToSaveCreatedUserAndReturnManagedUser()
            throws Exception {
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

    @Test(expected = UserNotFoundException.class)
    public void testUpdateUserWithNoMatchingUser_shouldDelegateToUserRepositoryAndThrowUserNotFoundException() {
        // Set up expectations
        when(userRepository.findOne(IRRELEVANT_USER_ID))
                .thenReturn(null);

        // Exercise SUT
        userCRUDService.updateUser(IRRELEVANT_USER_ID, IRRELEVANT_USER_EMAIL, IRRELEVANT_USER_PASSWORD,
                IRRELEVANT_USER_FIRST_NAME, IRRELEVANT_USER_LAST_NAME);
    }

    @Test(expected = DuplicateEmailException.class)
    public void testUpdateUserWithDuplicateEmail_shouldDelegateToUserRepositoryAndThrowDuplicateUserException() {
        // Set up fixture
        User userWithDuplicateEmail = aUser()
                .id(IRRELEVANT_USER_ID)
                .email(IRRELEVANT_USER_EMAIL)
                .password(IRRELEVANT_USER_PASSWORD)
                .firstName(IRRELEVANT_USER_FIRST_NAME)
                .lastName(IRRELEVANT_USER_LAST_NAME)
                .build();

        // Set up expectations
        when(userRepository.findOne(IRRELEVANT_USER_ID))
                .thenReturn(userWithDuplicateEmail);
        when(userRepository.save(userWithDuplicateEmail))
                .thenThrow(new DataIntegrityViolationException(IRRELEVANT_MESSAGE));

        // Exercise SUT
        userCRUDService.updateUser(IRRELEVANT_USER_ID, IRRELEVANT_USER_EMAIL, IRRELEVANT_USER_PASSWORD,
                IRRELEVANT_USER_FIRST_NAME, IRRELEVANT_USER_LAST_NAME);
    }

    @Test
    public void testUpdateUserWithValidUser_shouldDelegateToUserRepositoryAndReturnUpdatedUser() {
        // Set up fixture
        User expectedUser = aUser()
                .id(IRRELEVANT_USER_ID)
                .email(IRRELEVANT_USER_EMAIL)
                .password(IRRELEVANT_USER_PASSWORD)
                .firstName(IRRELEVANT_USER_FIRST_NAME)
                .lastName(IRRELEVANT_USER_LAST_NAME)
                .build();

        // Set up expectations
        when(userRepository.findOne(IRRELEVANT_USER_ID))
                .thenReturn(expectedUser);
        when(userRepository.save(expectedUser))
                .thenReturn(expectedUser);

        // Exercise SUT
        User actualUser = userCRUDService.updateUser(IRRELEVANT_USER_ID, IRRELEVANT_USER_EMAIL, IRRELEVANT_USER_PASSWORD,
                IRRELEVANT_USER_FIRST_NAME, IRRELEVANT_USER_LAST_NAME);

        assertThat(actualUser, is(expectedUser));
    }

    @Test(expected = UserNotFoundException.class)
    public void testDeleteUserWithNoMatchingUser_shouldDelegateToUserRepositoryAndThrowUserNotFoundException() {
        // Set up expectations
        doThrow(new EmptyResultDataAccessException(1)).when(userRepository).delete(IRRELEVANT_USER_ID);

        // Exercise SUT
        userCRUDService.deleteUser(IRRELEVANT_USER_ID);
    }

    @Test
    public void testDeleteUserWithMatchingUser_shouldDelegateToUserRepositoryToDeleteUser() {
        // Set up expectations
        doNothing().when(userRepository).delete(IRRELEVANT_USER_ID);

        // Exercise SUT
        userCRUDService.deleteUser(IRRELEVANT_USER_ID);
    }
}