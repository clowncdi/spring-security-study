package com.example.springsecuritystudy.jwt;

import java.security.Key;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.SigningKeyResolverAdapter;
import lombok.RequiredArgsConstructor;

/**
 * JwsHeader를 통해 Signature 검증에 필요한 Key를 가져오는 코드를 구현합니다.
 */
@RequiredArgsConstructor
public class SigningKeyResolver extends SigningKeyResolverAdapter {
	private final JwtKey jwtKey;

	@Override
	public Key resolveSigningKey(JwsHeader jwsHeader, Claims claims) {
		String kid = jwsHeader.getKeyId();
		if (kid == null)
			return null;
		return jwtKey.getKey(kid);
	}
}
