package com.example.springsecuritystudy.admin;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.springsecuritystudy.note.Note;
import com.example.springsecuritystudy.note.NoteService;
import com.example.springsecuritystudy.user.User;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

	private final NoteService noteService;

	/**
	 * 어드민인 경우 게시글 조회
	 * @return admin/index.html
	 */
	@GetMapping
	public String getPostForAdmin(Authentication authentication, Model model) {
		User user = (User) authentication.getPrincipal();
		List<Note> notes = noteService.findByUser(user);
		model.addAttribute("notes", notes);
		return "admin/index";
	}
}
