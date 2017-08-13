package com.trevorgowing.projectlog.security.token;

import com.trevorgowing.projectlog.security.constant.SecurityConstants;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TokenParser {

	@Value("${projectlog.authentication.secret}")
	private String authenticationSecret;

	public String parseAuthorizationHeader(String authorizationHeader) {
		return Jwts.parser()
				.setSigningKey(authenticationSecret)
				.parseClaimsJws(authorizationHeader.replace(SecurityConstants.AUTHORIZATION_HEADER_PREFIX, ""))
				.getBody()
				.getSubject();
	}
}
