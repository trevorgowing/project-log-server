package com.trevorgowing.projectlog.security;

import com.trevorgowing.projectlog.security.token.TokenFactory;
import com.trevorgowing.projectlog.security.token.TokenFilter;
import com.trevorgowing.projectlog.security.token.TokenParser;
import com.trevorgowing.projectlog.security.token.constant.TokenConstants;
import org.springframework.http.HttpMethod;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	private final TokenParser tokenParser;
	private final TokenFactory tokenFactory;
	private final PasswordEncoder passwordEncoder;
	private final UserDetailsService userDetailsService;
	private final AuthenticationTokenFactory authenticationTokenFactory;

	SecurityConfiguration(TokenParser tokenParser, TokenFactory tokenFactory, PasswordEncoder passwordEncoder,
								 UserDetailsService userDetailsService,
								 AuthenticationTokenFactory authenticationTokenFactory) {
		this.tokenParser = tokenParser;
		this.tokenFactory = tokenFactory;
		this.passwordEncoder = passwordEncoder;
		this.userDetailsService = userDetailsService;
		this.authenticationTokenFactory = authenticationTokenFactory;
	}

	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {
		httpSecurity.csrf().disable().authorizeRequests()
				.antMatchers(HttpMethod.POST, TokenConstants.TOKENS_URL_PATH).permitAll()
				.anyRequest().authenticated()
				.and()
				.addFilter(new TokenFilter(Jackson2ObjectMapperBuilder.json().build(), tokenFactory, authenticationManager(), authenticationTokenFactory))
				.addFilter(new AuthenticationFilter(tokenParser, SecurityContextHolder.getContext(), authenticationManager(), authenticationTokenFactory));
	}

	@Override
	protected void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
		authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
	}
}
