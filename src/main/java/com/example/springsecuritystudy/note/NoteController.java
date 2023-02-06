package com.example.springsecuritystudy.note;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.springsecuritystudy.user.User;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/note")
public class NoteController {

	private final NoteService noteService;

	@GetMapping
	public String getPost(Authentication authentication, Model model) {
		User user = (User) authentication.getPrincipal();
		List<Note> notes = noteService.findByUser(user);
		model.addAttribute("notes", notes);
		return "note/index";
	}

	@PostMapping
	public String savePost(@ModelAttribute NoteDto noteDto, Authentication authentication) {
		User user = (User) authentication.getPrincipal();
		noteService.saveNote(user, noteDto.getTitle(), noteDto.getContent());
 		return "redirect:note";
	}

	@DeleteMapping
	public String deletePost(@RequestParam Long id, Authentication authentication) {
		User user = (User) authentication.getPrincipal();
		noteService.deleteNote(user, id);
		return "redirect:note";
	}
}
