package com.example.springsecuritystudy.notice;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.springsecuritystudy.post.PostDto;

import lombok.RequiredArgsConstructor;

/**
 * 공지사항 서비스 Controller
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("/notice")
public class NoticeController {

	private final NoticeService noticeService;

	@GetMapping
	public String findByPost(Model model) {
		List<Notice> notices = noticeService.findAll();
		model.addAttribute("notices", notices);
		return "notice/index";
	}

	@PostMapping
	public String savePost(@ModelAttribute PostDto postDto) {
		noticeService.saveNotice(postDto.getTitle(), postDto.getContent());
		return "redirect:notice";
	}

	@DeleteMapping
	public String deletePost(@RequestParam Long id) {
		noticeService.deleteNotice(id);
		return "redirect:notice";
	}
}
