package com.example.springsecuritystudy.jwt;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.util.Base64Utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

class JwtUtilsTest {

	String secretKey = "SecretKeyToGenJWTsSecretKeyToGenJWTsSecretKeyToGenJWTs";

	private void printToken(String token) {
		System.out.println("token: " + token);
		System.out.println("header: " + decodeToken(token.split("\\.")[0]));
		System.out.println("payload: " + decodeToken(token.split("\\.")[1]));
	}

	private String decodeToken(String token) {
		return new String(Base64Utils.decodeFromString(token));
	}

	@Test
	void test_okta_token() {
		Map<String, Object> claims = new HashMap<>();
		claims.put("sub", "test");
		claims.put("name", "dalichoi");
		claims.put("admin", "true");
		claims.put("exp", Instant.now().plusSeconds(60*60*24).getEpochSecond());
		claims.put("iat", Instant.now().getEpochSecond());

		String oktaToken = Jwts.builder()
				// .setHeaderParam("typ", "JWT")
				// .setHeaderParam("alg", "HS256")
				// .setHeaderParam("kid", "key1")
				// .setSubject("test")
				// .setIssuedAt(java.util.Date.from(Instant.now()))
				// .setExpiration(java.util.Date.from(Instant.now().plusSeconds(60*60*24)))
				.addClaims(claims)
				.signWith(Keys.hmacShaKeyFor(secretKey.getBytes()), SignatureAlgorithm.HS256)
				.compact();
		printToken(oktaToken);

		Jws<Claims> claimsJws = Jwts.parserBuilder()
				.setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))
				.build()
				.parseClaimsJws(oktaToken);
		System.out.println("claimsJws: " + claimsJws);
	}

}
