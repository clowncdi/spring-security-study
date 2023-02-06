package com.example.springsecuritystudy.config;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Configuration;

import com.example.springsecuritystudy.notice.NoticeService;
import com.example.springsecuritystudy.post.PostService;
import com.example.springsecuritystudy.user.User;
import com.example.springsecuritystudy.user.UserService;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class InitializeConfig {

	private final UserService userService;
	private final PostService postService;
	private final NoticeService noticeService;

	@PostConstruct
	public void adminAccount() {
		User user = userService.signup("user", "user");
		userService.signupAdmin("admin", "admin");
		postService.savePost(user, "테스트", "테스트입니다.");
		postService.savePost(user, "테스트2", "테스트2입니다.");
		postService.savePost(user, "테스트3", "테스트3입니다.");
		postService.savePost(user, "여름 여행계획", "여름 여행계획 작성중...");
		noticeService.saveNotice("환영합니다", "환영합니다 여러분");
	}
}