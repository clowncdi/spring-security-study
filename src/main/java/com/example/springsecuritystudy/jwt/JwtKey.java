package com.example.springsecuritystudy.jwt;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.annotation.PostConstruct;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.security.Keys;
import javafx.util.Pair;

/**
 * JWT Key를 제공하고 조회한다.
 * Key Rolling을 지원한다.
 */
@Component
public class JwtKey {

	private final Environment env;

	public JwtKey(Environment env) {
		this.env = env;
	}

	private Map<String, String> SECRET_KEY_SET;
	private String[] KID_SET;
	private Random randomIndex;


	@PostConstruct
	public void init() {
		SECRET_KEY_SET = new HashMap<String, String>() {
			{
				put("key1", env.getProperty("jwt.secret-key1"));
				put("key2", env.getProperty("jwt.secret-key2"));
				put("key3", env.getProperty("jwt.secret-key3"));
			}
		};
		KID_SET = SECRET_KEY_SET.keySet().toArray(new String[0]);
		randomIndex = new Random();
	}

	/**
	 * SECRET_KEY_SET 에서 랜덤한 KEY 가져오기
	 *
	 * @return kid와 key Pair
	 */
	public Pair<String, Key> getRandomKey() {
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
	public Key getKey(String kid) {
		String key = SECRET_KEY_SET.getOrDefault(kid, null);
		if (key == null) {
			return null;
		}
		return Keys.hmacShaKeyFor(key.getBytes(StandardCharsets.UTF_8));
	}

}
