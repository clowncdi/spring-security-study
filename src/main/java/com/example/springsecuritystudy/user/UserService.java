package com.example.springsecuritystudy.user;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.springsecuritystudy.common.AlreadyRegisteredUserException;
import com.example.springsecuritystudy.common.UserNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public User signup(String username, String password) {
		alreadyRegisteredUser(username);
		return userRepository.save(new User(username, passwordEncoder.encode(password), "ROLE_USER"));
	}

	public User signupAdmin(String username, String password) {
		alreadyRegisteredUser(username);
		return userRepository.save(new User(username, passwordEncoder.encode(password), "ROLE_ADMIN"));
	}

	public User findByUsername(String username) {
		return userRepository.findByUsername(username)
				.orElseThrow(UserNotFoundException::new);
	}

	private void alreadyRegisteredUser(String username) {
		if (userRepository.findByUsername(username).isPresent()) {
			throw new AlreadyRegisteredUserException();
		}
	}

}
