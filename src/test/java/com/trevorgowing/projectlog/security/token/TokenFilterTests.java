package com.trevorgowing.projectlog.security.token;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trevorgowing.projectlog.common.types.AbstractTests;
import com.trevorgowing.projectlog.security.AuthenticationTokenFactory;
import com.trevorgowing.projectlog.security.constant.SecurityConstants;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TokenFilterTests extends AbstractTests {

	@Mock
	private ServletInputStream servletInputStream;
	@Mock
	private HttpServletRequest request;
	@Mock
	private HttpServletResponse response;
	@Mock
	private FilterChain filterChain;

	@Mock
	private ObjectMapper objectMapper;
	@Mock
	private TokenFactory tokenFactory;
	@Mock
	private AuthenticationManager authenticationManager;
	@Mock
	private AuthenticationTokenFactory authenticationTokenFactory;

	@InjectMocks
	private TokenFilter tokenFilter;

	@Test(expected = RuntimeException.class)
	public void testAttemptAuthenticationWithIOException_shouldThrowRuntimeException() throws IOException {
		// Set up expectations
		when(request.getInputStream()).thenThrow(new IOException());

		// Exercise SUT
		tokenFilter.attemptAuthentication(request, response);
	}

	@Test
	public void testAttemptAuthentication_shouldReturnAuthentication() throws IOException {
		// Set up expectations
		User user = new User("username", "password", Collections.emptySet());
		Authentication expectedAuthenticationToken = new UsernamePasswordAuthenticationToken("username",
				"password", Collections.emptyList());

		// Set up expectations
		when(request.getInputStream()).thenReturn(servletInputStream);
		when(objectMapper.readValue(servletInputStream, User.class)).thenReturn(user);
		when(authenticationTokenFactory.createAuthenticationToken("username", "password"))
				.thenReturn(expectedAuthenticationToken);
		when(authenticationManager.authenticate(expectedAuthenticationToken)).thenReturn(expectedAuthenticationToken);

		// Exercise SUT
		Authentication actualAuthenticationToken = tokenFilter.attemptAuthentication(request, response);

		// Verify behaviour
		verify(authenticationTokenFactory).createAuthenticationToken("username", "password");
		verify(authenticationManager).authenticate(expectedAuthenticationToken);
		assertThat(actualAuthenticationToken, is(expectedAuthenticationToken));
	}

	@Test
	public void testSuccessfulAuthentication_shouldSetAuthorizationHeader() throws IOException, ServletException {
		// Set up fixture
		String email = "email";
		String authorizationHeader = SecurityConstants.AUTHORIZATION_HEADER_PREFIX + ' ' + email;
		Authentication authenticationToken = new UsernamePasswordAuthenticationToken("email", null,
				Collections.emptyList());

		// Set up expectations
		when(tokenFactory.generateToken(email)).thenReturn(email);

		// Exercise SUT
		tokenFilter.successfulAuthentication(request, response, filterChain, authenticationToken);

		// Verify behaviour
		verify(response).addHeader(HttpHeaders.AUTHORIZATION, authorizationHeader);
	}
}
