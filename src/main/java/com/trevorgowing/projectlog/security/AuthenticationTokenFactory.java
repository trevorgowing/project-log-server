package com.trevorgowing.projectlog.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class AuthenticationTokenFactory {

	Authentication createAuthenticationToken(String email) {
		return new UsernamePasswordAuthenticationToken(email, null, new ArrayList<>());
	}

	public Authentication createAuthenticationToken(String email, String password) {
		return new UsernamePasswordAuthenticationToken(email, password, new ArrayList<>());
	}
}
