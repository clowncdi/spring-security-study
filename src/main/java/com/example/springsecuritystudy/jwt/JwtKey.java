package com.example.springsecuritystudy.jwt;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import io.jsonwebtoken.security.Keys;
import javafx.util.Pair;

/**
 * JWT Key를 제공하고 조회한다.
 * Key Rolling을 지원한다.
 */
public class JwtKey {
	/**
	 * Kid-Key List 외부로 절대 유출되어서는 안된다.
	 */
	private static final Map<String, String> SECRET_KEY_SET = new HashMap<String, String>() {
		{
			put("key1", "SpringSecurityJWTPracticeProjectIsSoGoodAndThisProjectIsSoFunSpringSecurityJWTPracticeProjectIsSoGoodAndThisProjectIsSoFun");
			put("key2", "GoodSpringSecurityNiceSpringSecurityGoodSpringSecurityNiceSpringSecurityGoodSpringSecurityNiceSpringSecurityGoodSpringSecurityNiceSpringSecurity");
			put("key3", "HelloSpringSecurityHelloSpringSecurityHelloSpringSecurityHelloSpringSecurityHelloSpringSecurityHelloSpringSecurityHelloSpringSecurityHelloSpringSecurity");
		}
	};

	private static final String[] KID_SET = SECRET_KEY_SET.keySet().toArray(new String[0]);
	private static Random randomIndex = new Random();

	/**
	 * SECRET_KEY_SET 에서 랜덤한 KEY 가져오기
	 *
	 * @return kid와 key Pair
	 */
	public static Pair<String, Key> getRandomKey() {
		String kid = KID_SET[randomIndex.nextInt(KID_SET.length)];
		String secretKey = SECRET_KEY_SET.get(kid);
		return new Pair<>(kid, Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)));
	}

	/**
	 * kid로 Key찾기
	 *
	 * @param kid kid
	 * @return Key
	 */
	public static Key getKey(String kid) {
		String key = SECRET_KEY_SET.getOrDefault(kid, null);
		if (key == null) {
			return null;
		}
		return Keys.hmacShaKeyFor(key.getBytes(StandardCharsets.UTF_8));
	}

}
