package com.example.springsecuritystudy.notice;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.example.springsecuritystudy.helper.TestConfig;
import com.example.springsecuritystudy.helper.WithMockAdmin;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class NoticeControllerTest extends TestConfig {

	@Autowired
	private NoticeRepository noticeRepository;
	@Autowired
	private ObjectMapper objectMapper;
	private MockMvc mvc;

	@BeforeEach
	public void setUp(@Autowired WebApplicationContext applicationContext) {
		this.mvc = MockMvcBuilders.webAppContextSetup(applicationContext)
				.apply(springSecurity())
				.alwaysDo(print())
				.build();
	}

	@Test
	void getNotice_인증없음() throws Exception {
		mvc.perform(get("/notice"))
				.andExpect(status().is3xxRedirection());
	}

	@Test
	@WithMockUser
	void getNotice_인증있음() throws Exception {
		mvc.perform(get("/notice"))
				.andExpect(status().isOk());
	}

	@Test
	void postNotice_인증없음() throws Exception {
		String content = objectMapper.writeValueAsString(new Notice("제목", "내용"));
		mvc.perform(post("/notice")
						.content(content))
				.andExpect(status().is4xxClientError());
	}

	@Test
	@WithMockUser(roles = "USER", username = "user", password = "user")
	void postNotice_유저인증있음() throws Exception {
		String content = objectMapper.writeValueAsString(new Notice("제목", "내용"));
		mvc.perform(post("/notice")
						.with(csrf())
						.content(content))
				.andExpect(status().is4xxClientError());
	}

	@Test
	@WithMockAdmin
	void postNotice_어드민인증있음() throws Exception {
		String content = objectMapper.writeValueAsString(new Notice("제목", "내용"));
		mvc.perform(post("/notice")
						.with(csrf())
						.content(content))
				.andExpect(redirectedUrl("notice"))
				.andExpect(status().is3xxRedirection());
	}

	@Test
	void deleteNotice_인증없음() throws Exception {
		Notice notice = noticeRepository.save(new Notice("제목", "내용"));
		mvc.perform(delete("/notice/" + notice.getId()))
				.andExpect(status().is4xxClientError());
	}

	@Test
	@WithMockUser(roles = "USER", username = "user", password = "user")
	void deleteNotice_유저인증있음() throws Exception {
		Notice notice = noticeRepository.save(new Notice("제목", "내용"));
		mvc.perform(delete("/notice/" + notice.getId())
						.with(csrf()))
				.andExpect(status().is4xxClientError());
	}

	@Test
	@WithMockAdmin
	void deleteNotice_어드민인증있음() throws Exception {
		Notice notice = noticeRepository.save(new Notice("제목", "내용"));
		mvc.perform(delete("/notice/" + notice.getId())
						.with(csrf()))
				.andExpect(status().is4xxClientError());
	}


}
