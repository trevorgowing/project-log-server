package com.trevorgowing.projectlog.user;

import com.trevorgowing.projectlog.common.types.AbstractTests;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.dao.DataIntegrityViolationException;

import static com.trevorgowing.projectlog.user.UserBuilder.aUser;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.samePropertyValuesAs;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.when;

public class UserFactoryTests extends AbstractTests {

	@Mock
	private UserRepository userRepository;

	@InjectMocks
	private UserFactory userFactory;

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
		userFactory.createUser(IRRELEVANT_USER_EMAIL, IRRELEVANT_USER_PASSWORD,
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
		when(userRepository.save(argThat(samePropertyValuesAs(unidentifiedUser))))
				.thenReturn(expectedUser);

		// Exercise SUT
		User actualUser = userFactory.createUser(IRRELEVANT_USER_EMAIL, IRRELEVANT_USER_PASSWORD,
				IRRELEVANT_USER_FIRST_NAME, IRRELEVANT_USER_LAST_NAME);

		// Verify behaviour
		assertThat(actualUser, is(expectedUser));
	}
}
