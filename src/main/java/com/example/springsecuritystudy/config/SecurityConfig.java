package com.example.springsecuritystudy.config;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.example.springsecuritystudy.user.UserRepository;

import lombok.RequiredArgsConstructor;

@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	private final UserRepository userRepository;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
				.authorizeHttpRequests(auth -> auth
						.antMatchers("/", "/home", "/signup", "/example",
								"/css/**", "/js/**", "/h2-console/**").permitAll()
						.antMatchers("/post").hasRole("USER")
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
		return web -> web.ignoring().antMatchers("/css/**", "/js/**", "/h2-console/**");
	}

	@Bean
	public UserDetailsService userDetailsService() {
		return username -> userRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("유저를 찾지 못 했습니다."));
	}
}
