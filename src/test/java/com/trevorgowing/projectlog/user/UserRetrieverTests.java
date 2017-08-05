package com.trevorgowing.projectlog.user;

import com.trevorgowing.projectlog.common.types.AbstractTests;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Collections;
import java.util.List;

import static com.trevorgowing.projectlog.user.IdentifiedUserDTOBuilder.anIdentifiedUserDTO;
import static com.trevorgowing.projectlog.user.UserBuilder.aUser;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

public class UserRetrieverTests extends AbstractTests {
	
	@Mock
	private UserRepository userRepository;
	
	@InjectMocks
	private UserRetriever userRetriever;
	
	@Test(expected = UserNotFoundException.class)
	public void testFindUserWithNoMatchingUser_shouldThrowUserNotFoundException() {
		// Set up expectations
		when(userRepository.findOne(IRRELEVANT_USER_ID))
				.thenReturn(null);

		// Exercise SUT
		userRetriever.findUser(IRRELEVANT_USER_ID);
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
		User actualUser = userRetriever.findUser(IRRELEVANT_USER_ID);

		// Verify behaviour
		assertThat(actualUser, is(expectedUser));
	}

	@Test
	public void testFindIdentifiedUserDTOs_shouldDelegateToUserRepositoryAndReturnIdentifiedUserDTOs() {
		// Set up fixture
		List<IdentifiedUserDTO> expectedIdentifiedUserDTOs = Collections.emptyList();

		// Set up expectations
		when(userRepository.findIdentifiedUserDTOs())
				.thenReturn(expectedIdentifiedUserDTOs);

		// Exercise SUT
		List<IdentifiedUserDTO> actualIdentifiedUserDTOs = userRetriever.findIdentifiedUserDTOs();

		// Verify behaviour
		assertThat(actualIdentifiedUserDTOs, is(expectedIdentifiedUserDTOs));
	}

	@Test(expected = UserNotFoundException.class)
	public void testFindIdentifiedUserDTOByIdWithNoMatchingUser_shouldDelegateToUserRepositoryAndThrowUserNotFoundException() {
		// Set up expectations
		when(userRepository.findIdentifiedUserDTOById(IRRELEVANT_USER_ID))
				.thenReturn(null);

		// Exercise SUT
		userRetriever.findIdentifiedUserDTOById(IRRELEVANT_USER_ID);
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
		IdentifiedUserDTO actualIdentifiedUserDTO = userRetriever.findIdentifiedUserDTOById(IRRELEVANT_USER_ID);

		// Verify behaviour
		assertThat(actualIdentifiedUserDTO, is(expectedIdentifiedUserDTO));
	}
	
}
