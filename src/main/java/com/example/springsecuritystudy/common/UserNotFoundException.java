package com.example.springsecuritystudy.common;

public class UserNotFoundException extends RuntimeException {

	public UserNotFoundException(String message) {
		super(message);
	}

	public UserNotFoundException() {
		super("유저를 찾을 수 없습니다.");
	}
}
