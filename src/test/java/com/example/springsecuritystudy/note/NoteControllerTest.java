package com.example.springsecuritystudy.note;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
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
				.username("user123")
				.password("user")
				.authority("ROLE_USER")
				.build();
		User admin2 = User.builder()
				.username("admin123")
				.password("admin")
				.authority("ROLE_ADMIN")
				.build();
		this.user = userRepository.save(user2);
		this.admin = userRepository.save(admin2);
	}

	@Test
	void getNote_μΈμ¦μμ() throws Exception {
		mvc.perform(
				get("/note")
		).andExpect(redirectedUrlPattern("**/login"))
				.andExpect(status().is3xxRedirection());
	}

	@Test
	//WithUserDetails λ‘ νμ€νΈ νλ λ°©λ²
	@WithUserDetails(
			value = "user123", // userDetailsServiceλ₯Ό ν΅ν΄ κ°μ Έμ¬ μ μλ μ μ 
			userDetailsServiceBeanName = "userDetailServiceImpl", // UserDetailsService κ΅¬νμ²΄μ Bean
			setupBefore = TestExecutionEvent.TEST_EXECUTION // νμ€νΈ μ€ν μ§μ μ μ μ λ₯Ό κ°μ Έμ¨λ€.
	)
	void getNote_μΈμ¦μμ() throws Exception {
		mvc.perform(
				get("/note")
		).andExpect(status().isOk())
				.andExpect(view().name("note/index"))
				.andDo(print());
	}

	@Test
	void postNote_μΈμ¦μμ() throws Exception {
		mvc.perform(
				post("/note").with(csrf())
						.contentType(MediaType.APPLICATION_FORM_URLENCODED)
						.param("title", "μ λͺ©")
						.param("content", "λ΄μ©")
		).andExpect(redirectedUrlPattern("**/login"))
				.andExpect(status().is3xxRedirection());
	}

	@Test
	@WithUserDetails(
			value = "admin123",
			userDetailsServiceBeanName = "userDetailServiceImpl",
			setupBefore = TestExecutionEvent.TEST_EXECUTION
	)
	void postNote_μ΄λλ―ΌμΈμ¦μμ() throws Exception {
		mvc.perform(
				post("/note").with(csrf())
						.contentType(MediaType.APPLICATION_FORM_URLENCODED)
						.param("title", "μ λͺ©")
						.param("content", "λ΄μ©")
		).andExpect(status().isForbidden());
	}

	@Test
	@WithUserDetails(
			value = "user123",
			userDetailsServiceBeanName = "userDetailServiceImpl",
			setupBefore = TestExecutionEvent.TEST_EXECUTION
	)
	void postNote_μ μ μΈμ¦μμ() throws Exception {
		mvc.perform(
				post("/note").with(csrf())
						.contentType(MediaType.APPLICATION_FORM_URLENCODED)
						.param("title", "μ λͺ©")
						.param("content", "λ΄μ©")
		).andExpect(redirectedUrl("note"))
				.andExpect(status().is3xxRedirection());
	}

	@Test
	void deleteNote_μΈμ¦μμ() throws Exception {
		Note note = noteRepository.save(new Note("μ λͺ©", "λ΄μ©", user));
		mvc.perform(
				delete("/note?id=" + note.getId()).with(csrf())
		).andExpect(redirectedUrlPattern("**/login"))
				.andExpect(status().is3xxRedirection());
	}

	@Test
	@WithUserDetails(
			value = "user123",
			userDetailsServiceBeanName = "userDetailServiceImpl",
			setupBefore = TestExecutionEvent.TEST_EXECUTION
	)
	void deleteNote_μΈμ¦μμ() throws Exception {
		Note note = noteRepository.save(new Note("μ λͺ©", "λ΄μ©", user));
		mvc.perform(
				delete("/note?id=" + note.getId()).with(csrf())
		).andExpect(redirectedUrl("note"))
				.andExpect(status().is3xxRedirection());
	}

	@Test
	@WithUserDetails(
			value = "admin123",
			userDetailsServiceBeanName = "userDetailServiceImpl",
			setupBefore = TestExecutionEvent.TEST_EXECUTION
	)
	void deleteNote_μ΄λλ―Όκ³μ μμ() throws Exception {
		Note note = noteRepository.save(new Note("μ λͺ©", "λ΄μ©", user));
		mvc.perform(
				delete("/note?id=" + note.getId()).with(csrf())
		).andExpect(status().isForbidden());
	}

}
