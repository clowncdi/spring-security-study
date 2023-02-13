package com.example.springsecuritystudy.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.context.request.async.WebAsyncManagerIntegrationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.example.springsecuritystudy.filter.StopwatchFilter;
import com.example.springsecuritystudy.jwt.JwtAuthenticationFilter;
import com.example.springsecuritystudy.jwt.JwtAuthorizationFilter;
import com.example.springsecuritystudy.jwt.JwtProperties;
import com.example.springsecuritystudy.jwt.JwtUtils;
import com.example.springsecuritystudy.user.UserRepository;

import lombok.RequiredArgsConstructor;

/**
 * Security 설정 Config
 */
@EnableWebSecurity(debug = true)
@RequiredArgsConstructor
public class SecurityConfig {

	private final UserRepository userRepository;
	private final JwtUtils jwtUtils;

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}


	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		// stopwatch filter
		http.addFilterBefore(
				new StopwatchFilter(),
				WebAsyncManagerIntegrationFilter.class
		);
		// JWT filter
		http.addFilterBefore(
				new JwtAuthenticationFilter(authenticationManager(http.getSharedObject(AuthenticationConfiguration.class))
						, jwtUtils),
				UsernamePasswordAuthenticationFilter.class
		).addFilterBefore(
				new JwtAuthorizationFilter(userRepository, jwtUtils),
				BasicAuthenticationFilter.class
		);
		http
				.httpBasic().disable()
				.csrf().disable();
		http
				.rememberMe().disable()
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		http
				.authorizeHttpRequests(auth -> auth
						.antMatchers("/", "/home", "/signup").permitAll()
						.antMatchers("/note").hasRole("USER")
						.antMatchers("/admin").hasRole("ADMIN")
						.antMatchers(HttpMethod.POST, "/notice").hasRole("ADMIN")
						.antMatchers(HttpMethod.DELETE, "/notice").hasRole("ADMIN")
						.anyRequest().authenticated()
				)
				.formLogin(form -> form
						.loginPage("/login")
						.defaultSuccessUrl("/", false)
						.permitAll()
				)
				.logout(logout -> logout
						// .logoutUrl("/logout") // post 방식으로만 동작
						.logoutRequestMatcher(new AntPathRequestMatcher("/logout")) // get 방식으로도 동작
						.logoutSuccessUrl("/")
						.deleteCookies(JwtProperties.COOKIE_NAME)
						.invalidateHttpSession(true)
				);

		return http.build();
	}

	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
		// 정적 리소스 spring security 대상에서 제외
		return (web) -> web.ignoring()
				.antMatchers("/h2-console/**")
				.requestMatchers(PathRequest.toStaticResources().atCommonLocations())
				;
	}

}
