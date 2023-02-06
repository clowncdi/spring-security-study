package com.example.springsecuritystudy.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.example.springsecuritystudy.helper.TestConfig;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class UserControllerTest extends TestConfig {

	private MockMvc mvc;

	@BeforeEach
	void setUp(@Autowired WebApplicationContext context) {
		this.mvc = MockMvcBuilders.webAppContextSetup(context)
				.apply(springSecurity())
				.alwaysDo(print())
				.build();
	}

	@Test
	void signup() throws Exception {
		mvc.perform(
						post("/signup").with(csrf())
								.contentType(MediaType.APPLICATION_FORM_URLENCODED)
								.param("username", "user123")
								.param("password", "password")
				).andExpect(redirectedUrl("login"))
				.andExpect(status().is3xxRedirection());
	}
}
