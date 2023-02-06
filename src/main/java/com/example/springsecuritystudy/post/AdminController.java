package com.example.springsecuritystudy.post;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.springsecuritystudy.user.User;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

	private final PostService postService;

	/**
	 * 어드민인 경우 게시글 조회
	 * @return admin/index.html
	 */
	@GetMapping
	public String getPostForAdmin(Authentication authentication, Model model) {
		User user = (User) authentication.getPrincipal();
		List<Post> posts = postService.findByUser(user);
		model.addAttribute("posts", posts);
		return "admin/index";
	}
}
