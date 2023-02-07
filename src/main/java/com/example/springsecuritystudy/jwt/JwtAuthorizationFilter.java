package com.example.springsecuritystudy.jwt;

import java.io.IOException;
import java.util.Arrays;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.springsecuritystudy.common.UserNotFoundException;
import com.example.springsecuritystudy.user.User;
import com.example.springsecuritystudy.user.UserRepository;

/**
 * JWT를 이용한 인증
 */
public class JwtAuthorizationFilter extends OncePerRequestFilter {

	private final UserRepository userRepository;

	public JwtAuthorizationFilter(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
			FilterChain filterChain) throws ServletException, IOException {
		String token = null;
		try {
			// cookie에서 JWT token을 가져온다.
			token = Arrays.stream(request.getCookies())
					.filter(cookie -> cookie.getName().equals(JwtProperties.COOKIE_NAME)).findFirst()
					.map(Cookie::getValue)
					.orElse(null);
		} catch (Exception ignored) {
		}
		if (token != null) {
			try {
				Authentication authentication = getUsernamePasswordAuthenticationToken(token);
				SecurityContextHolder.getContext().setAuthentication(authentication);
			} catch (Exception e) {
				Cookie cookie = new Cookie(JwtProperties.COOKIE_NAME, null);
				cookie.setMaxAge(0);
				response.addCookie(cookie);
			}
		}
		filterChain.doFilter(request, response);
	}

	private Authentication getUsernamePasswordAuthenticationToken(String token) {
		String username = JwtUtils.getUsername(token);
		if (username != null) {
			User user = userRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);
			return new UsernamePasswordAuthenticationToken(
					user,
					null,
					user.getAuthorities()
			);
		}
		return null;
	}
}
