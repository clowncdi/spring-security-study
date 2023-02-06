package com.example.springsecuritystudy.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.StopWatch;
import org.springframework.web.filter.OncePerRequestFilter;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StopwatchFilter extends OncePerRequestFilter {
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
			FilterChain filterChain) throws ServletException, IOException {
		StopWatch stopWatch = new StopWatch(request.getServletPath());
		stopWatch.start();
		filterChain.doFilter(request, response);
		stopWatch.stop();
		// Log StopWatch '/login' : running time = 150545041 ns
		log.info(stopWatch.shortSummary());
	}
}
