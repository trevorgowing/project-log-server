package com.trevorgowing.projectlog.security.token;

import com.trevorgowing.projectlog.common.DateTimeService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TokenFactory {

	private final DateTimeService dateTimeService;

	@Value("${projectlog.authentication.secret}")
	private String authenticationSecret;
	@Value("${projectlog.authentication.duration.seconds}")
	private Long authenticationSeconds;

	public TokenFactory(DateTimeService dateTimeService) {
		this.dateTimeService = dateTimeService;
	}

	String generateToken(String email) {
		return Jwts.builder()
				.setSubject(email)
				.setExpiration(Date.from(dateTimeService.now().plusSeconds(authenticationSeconds)))
				.signWith(SignatureAlgorithm.HS512, authenticationSecret)
				.compact();
	}
}
