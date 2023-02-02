package com.example.springsecuritystudy.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.example.springsecuritystudy.user.UserRepository;

import lombok.RequiredArgsConstructor;

@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	private final PasswordEncoder passwordEncoder;
	private final UserRepository userRepository;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
				.authorizeHttpRequests(auth -> auth
					.antMatchers("/", "/home", "/signup", "/example").permitAll()
					.antMatchers("/post").hasRole("USER")
					.antMatchers("/admin").hasRole("ADMIN")
					.anyRequest().authenticated()
				)
				.formLogin(form -> form
					.loginPage("/login")
					.defaultSuccessUrl("/")
					.permitAll()
				)
				.logout(logout -> logout
					.deleteCookies("remove")
					.invalidateHttpSession(false)
					.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
					.logoutSuccessUrl("/")
				);

		return http.build();
	}

	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
		return web -> web.ignoring().antMatchers("/css/**", "/js/**", "/h2-console/**");
	}

	@Bean
	public UserDetailsService users() {
		UserDetails user = User.withUsername("user")
				.password(passwordEncoder.encode("user"))
				.roles("USER")
				.build();
		UserDetails admin = User.withUsername("admin")
				.password(passwordEncoder.encode("admin"))
				.roles("ADMIN")
				.build();
		UserDetails tester = User.withUsername("test")
				.password(passwordEncoder.encode("test"))
				.roles("ADMIN", "USER")
				.build();
		return new InMemoryUserDetailsManager(user, admin, tester);
	}
}
