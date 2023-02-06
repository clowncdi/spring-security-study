package com.example.springsecuritystudy.admin;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.example.springsecuritystudy.helper.TestConfig;
import com.example.springsecuritystudy.user.User;
import com.example.springsecuritystudy.user.UserRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class AdminControllerTest extends TestConfig {

	@Autowired
	private UserRepository userRepository;
	private MockMvc mvc;
	private User user;
	private User admin;

	@BeforeEach
	void setUp(@Autowired WebApplicationContext applicationContext) {
		this.mvc = MockMvcBuilders.webAppContextSetup(applicationContext)
				.apply(springSecurity())
				.alwaysDo(print())
				.build();
		user = userRepository.save(new User("user", "user", "ROLE_USER"));
		admin = userRepository.save(new User("admin", "admin", "ROLE_ADMIN"));
	}

	@Test
	void getNoteForAdmin_인증없음() throws Exception {
		mvc.perform(
						get("/admin").with(csrf())
				).andExpect(redirectedUrlPattern("**/login"))
				.andExpect(status().is3xxRedirection());
	}

	@Test
	void getNoteForAdmin_어드민인증있음() throws Exception {
		mvc.perform(
				get("/admin").with(csrf()).with(user(admin))
		).andExpect(status().is2xxSuccessful());
	}

	@Test
	void getNoteForAdmin_유저인증있음() throws Exception {
		mvc.perform(
				get("/admin").with(csrf()).with(user(user))
		).andExpect(status().isForbidden());
	}

}
