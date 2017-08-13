package com.trevorgowing.projectlog.security;

import com.trevorgowing.projectlog.common.types.AbstractTests;
import com.trevorgowing.projectlog.user.User;
import com.trevorgowing.projectlog.user.UserRepository;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collections;

import static com.trevorgowing.projectlog.user.UserBuilder.aUser;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.samePropertyValuesAs;
import static org.mockito.Mockito.when;

public class UserDetailsServiceTests extends AbstractTests {

	@Mock
	private UserRepository userRepository;

	@InjectMocks
	private UserDetailsServiceSupport userDetailsService;

	@Test(expected = UsernameNotFoundException.class)
	public void testLoadUserByUsernameWithUnrecognisedEmail_shouldThrowUsernameNotFoundException() {
		// Set up fixture
		String unrecognisedEmail = "unrecognised@trevorgowing.com";

		// Set up expectations
		when(userRepository.findByEmail(unrecognisedEmail)).thenReturn(null);

		// Exercise SUT
		userDetailsService.loadUserByUsername(unrecognisedEmail);
	}

	@Test
	public void testLoadUserByUsernameWithRecognisedEmail_shouldReturnUserDetails() {
		// Set up fixture
		String recognisedEmail = "recognised@trevorgowing.com";
		String password = "password";
		User expectedUser = aUser().email(recognisedEmail).password(password).build();
		org.springframework.security.core.userdetails.User expectedUserDetails =
				new org.springframework.security.core.userdetails.User(recognisedEmail, password,
						Collections.emptyList());

		// Set up expectations
		when(userRepository.findByEmail(recognisedEmail)).thenReturn(expectedUser);

		// Exercise SUT
		UserDetails actualUserDetails = userDetailsService.loadUserByUsername(recognisedEmail);

		// Verify state
		assertThat(actualUserDetails, samePropertyValuesAs(expectedUserDetails));
	}
}
