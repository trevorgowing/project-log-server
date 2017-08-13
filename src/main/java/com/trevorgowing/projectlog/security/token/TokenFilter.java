package com.trevorgowing.projectlog.security.token;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trevorgowing.projectlog.security.AuthenticationTokenFactory;
import com.trevorgowing.projectlog.security.constant.SecurityConstants;
import com.trevorgowing.projectlog.security.token.constant.TokenConstants;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class TokenFilter extends UsernamePasswordAuthenticationFilter {

	private final ObjectMapper objectMapper;
	private final TokenFactory tokenFactory;
	private final AuthenticationManager authenticationManager;
	private final AuthenticationTokenFactory authenticationTokenFactory;

	public TokenFilter(ObjectMapper objectMapper, TokenFactory tokenFactory,
					   AuthenticationManager authenticationManager,
					   AuthenticationTokenFactory authenticationTokenFactory) {
		this.objectMapper = objectMapper;
		this.tokenFactory = tokenFactory;
		this.authenticationManager = authenticationManager;
		this.authenticationTokenFactory = authenticationTokenFactory;
		setFilterProcessesUrl(TokenConstants.TOKENS_URL_PATH);
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		try {
			User user = objectMapper.readValue(request.getInputStream(), User.class);
			return authenticationManager.authenticate(authenticationTokenFactory.createAuthenticationToken(
					user.getUsername(), user.getPassword()));
		} catch (IOException ioe) {
			throw new RuntimeException(ioe);
		}
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
											Authentication authentication) throws IOException, ServletException {
		String authorizationHeader = new StringBuilder(SecurityConstants.AUTHORIZATION_HEADER_PREFIX).append(' ')
				.append(tokenFactory.generateToken(authentication.getName())).toString();
		response.addHeader(HttpHeaders.AUTHORIZATION, authorizationHeader);
	}
}
