package com.example.springsecuritystudy.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.springsecuritystudy.helper.TestConfig;

import static org.assertj.core.api.BDDAssertions.*;
import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest extends TestConfig {

	@Autowired
	private UserService userService;
	@Autowired
	private UserRepository userRepository;

	@Test
	void signup() {
		//given
		String username = "user123";
		String password = "password";
		//when
		User user = userService.signup(username, password);
		//then
		then(user.getId()).isNotNull();
		then(user.getUsername()).isEqualTo(username);
		then(user.getPassword()).startsWith("{bcrypt}");
		then(user.getAuthorities()).hasSize(1);
		then(user.getAuthorities().stream().findFirst().get().getAuthority()).isEqualTo("ROLE_USER");
		then(user.isAdmin()).isFalse();
		then(user.isAccountNonExpired()).isTrue();
		then(user.isAccountNonLocked()).isTrue();
		then(user.isEnabled()).isTrue();
		then(user.isCredentialsNonExpired()).isTrue();
	}

	@Test
	void signupAdmin() {
		//given
		String username = "admin123";
		String password = "password";
		//when
		User user = userService.signupAdmin(username, password);
		//then
		then(user.getId()).isNotNull();
		then(user.getUsername()).isEqualTo(username);
		then(user.getPassword()).startsWith("{bcrypt}");
		then(user.getAuthorities()).hasSize(1);
		then(user.getAuthorities().stream().findFirst().get().getAuthority()).isEqualTo("ROLE_ADMIN");
		then(user.isAdmin()).isTrue();
		then(user.isAccountNonExpired()).isTrue();
		then(user.isAccountNonLocked()).isTrue();
		then(user.isEnabled()).isTrue();
		then(user.isCredentialsNonExpired()).isTrue();
	}

	@Test
	void findByUsername() {
		//given
		User user = User.builder()
				.username("user123")
				.password("password")
				.authority("ROLE_USER")
				.build();
		userRepository.save(user);
		//when
		User savedUser = userService.findByUsername("user123");
		//then
		then(savedUser.getId()).isNotNull();
	}
}
