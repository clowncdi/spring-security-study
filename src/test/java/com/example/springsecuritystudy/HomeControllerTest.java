package com.example.springsecuritystudy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
class HomeControllerTest {

	@Autowired
	private WebApplicationContext applicationContext;
	private MockMvc mvc;

	@BeforeEach
	public void setUp() {
		mvc = MockMvcBuilders.webAppContextSetup(applicationContext)
				.apply(springSecurity())
				.alwaysDo(print())
				.build();
	}

	@Test
	void login_user() throws Exception {
		mvc.perform(
				formLogin("/login")
						.user("user")
						.password("user")
		).andExpect(status().is3xxRedirection());
	}

	@Test
	void login_admin() throws Exception {
		mvc.perform(
				formLogin("/login")
						.user("admin")
						.password("admin")
		).andExpect(status().is3xxRedirection());
	}

	@Test
	@WithMockUser
	void access_user() throws Exception {
		mvc.perform(
				get("/user")
		).andExpect(status().isOk());
	}

	@Test
	@WithMockAdmin
	void access_admin() throws Exception {
		mvc.perform(
				get("/admin")
		).andExpect(status().isOk());
	}

	@Test
	@WithMockUser
	void access_denied() throws Exception {
		mvc.perform(
				get("/admin")
		).andExpect(status().isForbidden());
	}
}
