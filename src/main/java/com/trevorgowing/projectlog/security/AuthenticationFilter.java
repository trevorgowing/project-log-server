package com.trevorgowing.projectlog.security;

import com.trevorgowing.projectlog.security.constant.SecurityConstants;
import com.trevorgowing.projectlog.security.token.TokenParser;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

class AuthenticationFilter extends BasicAuthenticationFilter {

	private final TokenParser tokenParser;
	private final SecurityContext securityContext;
	private final AuthenticationTokenFactory authenticationTokenFactory;

	AuthenticationFilter(TokenParser tokenParser, SecurityContext securityContext,
						 AuthenticationManager authenticationManager,
						 AuthenticationTokenFactory authenticationTokenFactory) {
		super(authenticationManager);
		this.tokenParser = tokenParser;
		this.securityContext = securityContext;
		this.authenticationTokenFactory = authenticationTokenFactory;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

		if (authorizationHeader == null || authorizationHeader.isEmpty()
				|| !authorizationHeader.startsWith(SecurityConstants.AUTHORIZATION_HEADER_PREFIX)) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			chain.doFilter(request, response);
			return;
		}

		securityContext.setAuthentication(authenticationTokenFactory.createAuthenticationToken(
				tokenParser.parseAuthorizationHeader(authorizationHeader)));

		chain.doFilter(request, response);
	}
}
