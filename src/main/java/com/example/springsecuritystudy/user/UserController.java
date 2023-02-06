package com.example.springsecuritystudy.user;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	@GetMapping("/login")
	public String loginView() {
		return "login";
	}

	@GetMapping("/signup")
	public String signupView() {
		return "signup";
	}

	@PostMapping
	public String signup(@ModelAttribute UserDto userDto) {
		userService.signup(userDto.getUsername(), userDto.getPassword());
		return "redirect:login";
	}
}
