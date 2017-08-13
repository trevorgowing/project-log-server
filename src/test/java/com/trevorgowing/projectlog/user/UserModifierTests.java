package com.trevorgowing.projectlog.user;

import com.trevorgowing.projectlog.common.types.AbstractTests;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;

import static com.trevorgowing.projectlog.user.UserBuilder.aUser;
import static com.trevorgowing.projectlog.user.UserNotFoundException.identifiedUserNotFoundException;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.samePropertyValuesAs;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.when;

public class UserModifierTests extends AbstractTests {

    @Mock
    private UserRetriever userRetriever;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserModifier userModifier;

    @Test(expected = UserNotFoundException.class)
    public void testUpdateUserWithNoMatchingUser_shouldDelegateToUserRepositoryAndThrowUserNotFoundException() {
        // Set up expectations
        when(userRetriever.findUser(IRRELEVANT_USER_ID))
                .thenThrow(identifiedUserNotFoundException(IRRELEVANT_USER_ID));

        // Exercise SUT
        userModifier.updateUser(IRRELEVANT_USER_ID, IRRELEVANT_USER_EMAIL, IRRELEVANT_USER_PASSWORD,
                IRRELEVANT_USER_FIRST_NAME, IRRELEVANT_USER_LAST_NAME);
    }

    @Test(expected = DuplicateEmailException.class)
    public void testUpdateUserWithDuplicateEmail_shouldDelegateToUserRepositoryAndThrowDuplicateUserException() {
        // Set up fixture
        User userWithDuplicateEmail = aUser()
                .id(IRRELEVANT_USER_ID)
                .email(IRRELEVANT_USER_EMAIL)
                .password(IRRELEVANT_ENCODED_PASSWORD)
                .firstName(IRRELEVANT_USER_FIRST_NAME)
                .lastName(IRRELEVANT_USER_LAST_NAME)
                .build();

        // Set up expectations
        when(userRetriever.findUser(IRRELEVANT_USER_ID))
                .thenReturn(userWithDuplicateEmail);
        when(passwordEncoder.encode(IRRELEVANT_USER_PASSWORD)).thenReturn(IRRELEVANT_ENCODED_PASSWORD);
        when(userRepository.save(userWithDuplicateEmail))
                .thenThrow(new DataIntegrityViolationException(IRRELEVANT_MESSAGE));

        // Exercise SUT
        userModifier.updateUser(IRRELEVANT_USER_ID, IRRELEVANT_USER_EMAIL, IRRELEVANT_USER_PASSWORD,
                IRRELEVANT_USER_FIRST_NAME, IRRELEVANT_USER_LAST_NAME);
    }

    @Test
    public void testUpdateUserWithValidUser_shouldDelegateToUserRepositoryAndReturnUpdatedUser() {
        // Set up fixture
        User expectedUser = aUser()
                .id(IRRELEVANT_USER_ID)
                .email(IRRELEVANT_USER_EMAIL)
                .password(IRRELEVANT_ENCODED_PASSWORD)
                .firstName(IRRELEVANT_USER_FIRST_NAME)
                .lastName(IRRELEVANT_USER_LAST_NAME)
                .build();

        // Set up expectations
        when(userRetriever.findUser(IRRELEVANT_USER_ID))
                .thenReturn(expectedUser);
        when(passwordEncoder.encode(IRRELEVANT_USER_PASSWORD)).thenReturn(IRRELEVANT_ENCODED_PASSWORD);
        when(userRepository.save(argThat(samePropertyValuesAs(expectedUser))))
                .thenReturn(expectedUser);

        // Exercise SUT
        User actualUser = userModifier.updateUser(IRRELEVANT_USER_ID, IRRELEVANT_USER_EMAIL, IRRELEVANT_USER_PASSWORD,
                IRRELEVANT_USER_FIRST_NAME, IRRELEVANT_USER_LAST_NAME);

        assertThat(actualUser, is(expectedUser));
    }
}