package com.example.springsecuritystudy.config;

import com.example.springsecuritystudy.filter.StopwatchFilter;
import com.example.springsecuritystudy.filter.TesterAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.request.async.WebAsyncManagerIntegrationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * Security 설정 Config
 */
@EnableWebSecurity(debug = true)
@RequiredArgsConstructor
public class SecurityConfig {

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
		// tester authentication filter
		http.addFilterBefore(
				new TesterAuthenticationFilter(authenticationManager(http.getSharedObject(AuthenticationConfiguration.class))),
				UsernamePasswordAuthenticationFilter.class
		);
		http
				.httpBasic().disable()
				.csrf();
		http
				.rememberMe();
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
		return (web) -> web.ignoring()
				.antMatchers("/h2-console/**")
				.requestMatchers(PathRequest.toStaticResources().atCommonLocations())
				;
	}

}
