package com.example.springsecuritystudy.jwt;

import java.security.Key;
import java.util.Date;

import com.example.springsecuritystudy.user.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.Jwts;
import javafx.util.Pair;

public class JwtUtils {
	/**
	 * 토큰에서 username 찾기
	 *
	 * @param token 토큰
	 * @return username
	 */
	public static String getUsername(String token) {
		// jwtToken에서 username을 찾는다.
		return Jwts.parserBuilder()
				.setSigningKeyResolver(SigningKeyResolver.instance)
				.build()
				.parseClaimsJws(token)
				.getBody()
				.getSubject(); // 토큰에 담긴 정보에서 username을 가져온다.
	}

	/**
	 * user로 토큰 생성
	 * HEADER : alg, kid
	 * PAYLOAD : sub, iat, exp
	 * SIGNATURE : JwtKey.getRandomKey로 구한 Secret Key로 HS512 해시
	 *
	 * @param user 유저
	 * @return jwt token
	 */
	public static String createToken(User user) {
		Claims claims = Jwts.claims().setSubject(user.getUsername());
		Date now = new Date();
		Pair<String, Key> key = JwtKey.getRandomKey();
		return Jwts.builder()
				.setClaims(claims) // 토큰에 담을 정보 설정
				.setIssuedAt(now) // 토큰 발행 시간 설정
				.setExpiration(new Date(now.getTime() + JwtProperties.EXPIRATION_TIME)) // 토큰 만료 시간 설정
				.setHeaderParam(JwsHeader.KEY_ID, key.getKey()) // 토큰에 kid 설정
				.signWith(key.getValue()) // signature 생성
				.compact();
	}

}
