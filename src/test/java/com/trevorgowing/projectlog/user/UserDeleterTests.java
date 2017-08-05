package com.trevorgowing.projectlog.user;

import com.trevorgowing.projectlog.common.types.AbstractTests;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.dao.EmptyResultDataAccessException;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;

public class UserDeleterTests extends AbstractTests {

	@Mock
	private UserRepository userRepository;

	@InjectMocks
	private UserDeleter userDeleter;

	@Test(expected = UserNotFoundException.class)
	public void testDeleteUserWithNoMatchingUser_shouldDelegateToUserRepositoryAndThrowUserNotFoundException() {
		// Set up expectations
		doThrow(new EmptyResultDataAccessException(1)).when(userRepository).delete(IRRELEVANT_USER_ID);

		// Exercise SUT
		userDeleter.deleteUser(IRRELEVANT_USER_ID);
	}

	@Test
	public void testDeleteUserWithMatchingUser_shouldDelegateToUserRepositoryToDeleteUser() {
		// Set up expectations
		doNothing().when(userRepository).delete(IRRELEVANT_USER_ID);

		// Exercise SUT
		userDeleter.deleteUser(IRRELEVANT_USER_ID);
	}
}
