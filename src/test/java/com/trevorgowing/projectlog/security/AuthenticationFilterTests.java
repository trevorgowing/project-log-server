package com.trevorgowing.projectlog.security;

import com.trevorgowing.projectlog.common.types.AbstractTests;
import com.trevorgowing.projectlog.security.constant.SecurityConstants;
import com.trevorgowing.projectlog.security.token.TokenParser;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AuthenticationFilterTests extends AbstractTests {

	@Mock
	private HttpServletRequest request;
	@Mock
	private HttpServletResponse response;
	@Mock
	private FilterChain filterChain;

	@Mock
	private TokenParser tokenParser;
	@Mock
	private SecurityContext securityContext;
	@Mock
	@SuppressWarnings("unused")
	private AuthenticationManager authenticationManager;
	@Mock
	private AuthenticationTokenFactory authenticationTokenFactory;

	@InjectMocks
	private AuthenticationFilter authenticationFilter;

	@Test
	public void testDoFilterInternalWithNullAuthorizationHeader_shouldSetResponseStatusToForbiddenAndContinueFilterChain() throws IOException, ServletException {
		// Set up expectations
		when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn(null);

		// Exercise SUT
		authenticationFilter.doFilterInternal(request, response, filterChain);

		// Verify behaviour
		verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		verify(filterChain).doFilter(request, response);
	}

	@Test
	public void testDoFilterInternalWithEmptyAuthorizationHeader_shouldSetResponseStatusToForbiddenAndContinueFilterChain() throws IOException, ServletException {
		// Set up expectations
		when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("");

		// Exercise SUT
		authenticationFilter.doFilterInternal(request, response, filterChain);

		// Verify behaviour
		verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		verify(filterChain).doFilter(request, response);
	}

	@Test
	public void testDoFilterInternalWithAuthorizationHeaderWithUnexpectedSchema_shouldSetResponseStatusToForbiddenAndContinueFilterChain() throws IOException, ServletException {
		// Set up fixture
		String authorizationHeaderWithUnexpectedSchema = "Basic trevor:password";

		// Set up expectations
		when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn(authorizationHeaderWithUnexpectedSchema);

		// Exercise SUT
		authenticationFilter.doFilterInternal(request, response, filterChain);

		// Verify behaviour
		verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		verify(filterChain).doFilter(request, response);
	}

	@Test
	public void testDoFilterInternalWithAuthorizationHeaderWithExpectedSchema_shouldSetAuthenticationAndContinueFilter() throws IOException, ServletException {
		// Set up fixture
		String authorizationHeaderWithExpectedSchema = SecurityConstants.AUTHORIZATION_HEADER_PREFIX + ' ' + "jwt";
		String email = "email";
		Authentication authenticationToken = new UsernamePasswordAuthenticationToken(email, null,
				new ArrayList<>());

		// Set up expectations
		when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn(authorizationHeaderWithExpectedSchema);
		when(tokenParser.parseAuthorizationHeader(authorizationHeaderWithExpectedSchema)).thenReturn(email);
		when(authenticationTokenFactory.createAuthenticationToken(email)).thenReturn(authenticationToken);

		// Exercise SUT
		authenticationFilter.doFilterInternal(request, response, filterChain);

		// Verify behaviour
		verify(tokenParser).parseAuthorizationHeader(authorizationHeaderWithExpectedSchema);
		verify(authenticationTokenFactory).createAuthenticationToken(email);
		verify(securityContext).setAuthentication(authenticationToken);
		verify(filterChain).doFilter(request, response);
	}
}
