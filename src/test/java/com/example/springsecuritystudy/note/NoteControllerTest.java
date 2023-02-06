package com.example.springsecuritystudy.note;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.example.springsecuritystudy.helper.TestConfig;
import com.example.springsecuritystudy.user.User;
import com.example.springsecuritystudy.user.UserRepository;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class NoteControllerTest extends TestConfig {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private NoteRepository noteRepository;
	private MockMvc mvc;
	private User user;
	private User admin;

	@BeforeEach
	public void setUp(@Autowired WebApplicationContext applicationContext) {
		this.mvc = MockMvcBuilders.webAppContextSetup(applicationContext)
				.apply(springSecurity())
				.alwaysDo(print())
				.build();
		User user2 = User.builder()
				.username("user")
				.password("user")
				.authority("ROLE_USER")
				.build();
		User admin2 = User.builder()
				.username("admin")
				.password("admin")
				.authority("ROLE_ADMIN")
				.build();
		this.user = userRepository.save(user2);
		this.admin = userRepository.save(admin2);
	}

	@Test
	void getNote_인증없음() throws Exception {
		mvc.perform(
				get("/note")
		).andExpect(status().is3xxRedirection());
	}

	@Test
	void getNote_인증있음() throws Exception {
		mvc.perform(
				get("/note").with(user(user))
		).andExpect(status().isOk());
	}

	@Test
	void postNote_인증없음() throws Exception {
		mvc.perform(
				post("/note").with(csrf())
						.contentType(MediaType.APPLICATION_FORM_URLENCODED)
						.param("title", "제목")
						.param("content", "내용")
		).andExpect(status().is3xxRedirection());
	}

	@Test
	void postNote_어드민인증있음() throws Exception {
		mvc.perform(
				post("/note").with(csrf()).with(user(admin))
						.contentType(MediaType.APPLICATION_FORM_URLENCODED)
						.param("title", "제목")
						.param("content", "내용")
		).andExpect(status().isForbidden());
	}

	@Test
	void postNote_유저인증있음() throws Exception {
		mvc.perform(
				post("/note").with(csrf()).with(user(user))
						.contentType(MediaType.APPLICATION_FORM_URLENCODED)
						.param("title", "제목")
						.param("content", "내용")
		).andExpect(redirectedUrl("note"))
				.andExpect(status().is3xxRedirection());
	}

	@Test
	void deleteNote_인증없음() throws Exception {
		Note note = noteRepository.save(new Note("제목", "내용", user));
		mvc.perform(
				delete("/note?id=" + note.getId()).with(csrf())
		).andExpect(status().is3xxRedirection());
	}

	@Test
	void deleteNote_인증있음() throws Exception {
		Note note = noteRepository.save(new Note("제목", "내용", user));
		mvc.perform(
				delete("/note?id=" + note.getId()).with(csrf()).with(user(user))
		).andExpect(redirectedUrl("note")).andExpect(status().is3xxRedirection());
	}

	@Test
	void deleteNote_어드민계정있음() throws Exception {
		Note note = noteRepository.save(new Note("제목", "내용", user));
		mvc.perform(
				delete("/note?id=" + note.getId()).with(csrf()).with(user(admin))
		).andExpect(status().is4xxClientError());
	}

}
