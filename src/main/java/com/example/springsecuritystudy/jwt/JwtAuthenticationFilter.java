package com.example.springsecuritystudy.jwt;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.springsecuritystudy.user.User;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private final JwtUtils jwtUtils;

	public JwtAuthenticationFilter(
			AuthenticationManager authenticationManager, JwtUtils jwtUtils) {
		super(authenticationManager);
		this.jwtUtils = jwtUtils;
	}

	/**
	 * 로그인 인증 시도
	 */
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws
			AuthenticationException {
		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
				request.getParameter("username"),
				request.getParameter("password"),
				new ArrayList<>()
		);
		return getAuthenticationManager().authenticate(authenticationToken);
	}

	/**
	 * 인증에 성공했을 때 사용
	 * JWT Token을 생성해서 쿠키에 넣는다.
	 */
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		User user = (User)authResult.getPrincipal();
		String token = jwtUtils.createToken(user);
		// 쿠키 생성
		Cookie cookie = new Cookie(JwtProperties.COOKIE_NAME, token);
		cookie.setMaxAge(JwtProperties.EXPIRATION_TIME); // 쿠키 만료 시간
		cookie.setPath("/");
		response.addCookie(cookie);
		response.sendRedirect("/");
	}

	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException failed) throws IOException, ServletException {
		response.sendRedirect("/login");
	}
}
