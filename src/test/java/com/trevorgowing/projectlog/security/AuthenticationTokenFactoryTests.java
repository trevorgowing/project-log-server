package com.trevorgowing.projectlog.security;

import com.trevorgowing.projectlog.common.types.AbstractTests;
import org.junit.Test;
import org.springframework.security.core.Authentication;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class AuthenticationTokenFactoryTests extends AbstractTests {

	private AuthenticationTokenFactory authenticationTokenFactory = new AuthenticationTokenFactory();

	@Test
	public void testCreateAuthenticationWithEmail_shouldCreateUsernamePasswordAuthentication() {
		// Set up fixture
		String email = "email";

		// Exercise SUT
		Authentication authentication = authenticationTokenFactory.createAuthenticationToken(email);

		// Verify behaviour
		assertThat(authentication.getName(), is(email));
	}

	@Test
	public void testCreateAuthenticationWithEmailAndPassword_shouldCreateUsernamePasswordAuthentication() {
		// Set up fixture
		String email = "email";
		String password = "password";

		// Exercise SUT
		Authentication authentication = authenticationTokenFactory.createAuthenticationToken(email, password);

		// Verify behaviour
		assertThat(authentication.getName(), is(email));
		assertThat(authentication.getCredentials(), is(password));
	}
}
