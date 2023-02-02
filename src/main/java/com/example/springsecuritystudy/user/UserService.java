package com.example.springsecuritystudy.user;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public User signup(String username, String password) {
		if (userRepository.findByUsername(username).isPresent()) {
			throw new RuntimeException("이미 등록된 유저입니다.");
		}
		return userRepository.save(new User(username, passwordEncoder.encode(password), "ROLE_USER"));
	}

	public User signupAdmin(String username, String password) {
		if (userRepository.findByUsername(username).isPresent()) {
			throw new RuntimeException("이미 등록된 Admin유저입니다.");
		}
		return userRepository.save(new User(username, passwordEncoder.encode(password), "ROLE_ADMIN"));
	}

	public User findByUsername(String username) {
		return userRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 유저입니다."));
	}

}
