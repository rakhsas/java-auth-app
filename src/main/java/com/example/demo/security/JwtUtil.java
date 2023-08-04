package com.example.demo.security;

import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.JWTVerifier;

@Component
public class JwtUtil {
	@Value("${jwt_secret}")
	public String	secret;

	public String generateToken(String username) throws IllegalArgumentException, JWTCreationException
	{
		return JWT.create()
				.withSubject("user Details")
				.withClaim("username", username)
				.withIssuedAt(new Date())
				.withIssuer("Rakhsas")
				.sign(Algorithm.HMAC256("GRIZOO"));
	}

	public String validateToken(String token) throws JWTVerificationException
	{
		JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256("GRIZOO"))
			.withSubject("user Details")
			.withIssuer("Rakhsas")
			.build();
		DecodedJWT jwt = jwtVerifier.verify(token);
		return jwt.getClaim("username").asString();
	}
}