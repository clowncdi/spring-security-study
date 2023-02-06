package com.example.springsecuritystudy.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import lombok.RequiredArgsConstructor;

/**
 * Security 설정 Config
 */
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
				.httpBasic().disable()
				.csrf();
		http
				.rememberMe();
		http
				.authorizeHttpRequests(auth -> auth
						.antMatchers("/", "/home", "/signup", "/h2-console/**").permitAll()
						.antMatchers("/note").hasRole("USER")
						.antMatchers("/admin").hasRole("ADMIN")
						.antMatchers(HttpMethod.POST, "/notice").hasRole("ADMIN")
						.antMatchers(HttpMethod.DELETE, "/notice").hasRole("ADMIN")
						.anyRequest().authenticated()
				)
				.formLogin(form -> form
						.loginPage("/login")
						.defaultSuccessUrl("/")
						.permitAll()
				)
				.logout(logout -> logout
						// .logoutUrl("/logout") // post 방식으로만 동작
						.logoutRequestMatcher(new AntPathRequestMatcher("/logout")) // get 방식으로도 동작
						.logoutSuccessUrl("/")
						.deleteCookies("JSESSIONID")
						.invalidateHttpSession(true)
				);

		return http.build();
	}

	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
		// 정적 리소스 spring security 대상에서 제외
		return (web) -> web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
	}

}
