package com.example.springsecuritystudy.note;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.springsecuritystudy.helper.TestConfig;
import com.example.springsecuritystudy.user.User;
import com.example.springsecuritystudy.user.UserService;

import static org.assertj.core.api.BDDAssertions.*;

class NoteServiceTest extends TestConfig {

	@Autowired
	private NoteService noteService;
	@Autowired
	private UserService userService;

	@Test
	void findByUser_유저가_게시물조회() {
		//given
		User user = userService.signup("user", "user");
		noteService.saveNote(user, "title1", "content1");
		noteService.saveNote(user, "title2", "content2");
		//when
		List<Note> notes = noteService.findByUser(user);
		//then
		then(notes.size()).isEqualTo(2);
		Note note1 = notes.get(0);
		Note note2 = notes.get(1);

		// post1 = title2
		then(note1.getUser().getUsername()).isEqualTo("user");
		then(note1.getTitle()).isEqualTo("title2");
		then(note1.getContent()).isEqualTo("content2");
		// post2 = title1
		then(note2.getUser().getUsername()).isEqualTo("user");
		then(note2.getTitle()).isEqualTo("title1");
		then(note2.getContent()).isEqualTo("content1");
	}

	@Test
	void findByUser_어드민이_조회() {
		// given
		User admin = userService.signupAdmin("admin", "admin");
		User user1 = userService.signup("user1", "user1");
		User user2 = userService.signup("user2", "user2");
		noteService.saveNote(user1, "title1", "content1");
		noteService.saveNote(user1, "title2", "content2");
		noteService.saveNote(user2, "title3", "content3");
		// when
		List<Note> notes = noteService.findByUser(admin);
		// then
		then(notes.size()).isEqualTo(3);
		Note note1 = notes.get(0);
		Note note2 = notes.get(1);
		Note note3 = notes.get(2);

		// post1 = title3
		then(note1.getUser().getUsername()).isEqualTo("user2");
		then(note1.getTitle()).isEqualTo("title3");
		then(note1.getContent()).isEqualTo("content3");
		// post1 = title2
		then(note2.getUser().getUsername()).isEqualTo("user1");
		then(note2.getTitle()).isEqualTo("title2");
		then(note2.getContent()).isEqualTo("content2");
		// post1 = title1
		then(note3.getUser().getUsername()).isEqualTo("user1");
		then(note3.getTitle()).isEqualTo("title1");
		then(note3.getContent()).isEqualTo("content1");
	}

	@Test
	void savePost() {
		// given
		User user = userService.signup("user", "user");
		// when
		noteService.saveNote(user, "title", "content");
		// then
		then(noteService.findByUser(user).size()).isEqualTo(1);
	}

	@Test
	void deletePost() {
		// given
		User user = userService.signup("user", "user");
		Note note = noteService.saveNote(user, "title", "content");
		// when
		noteService.deleteNote(user, note.getId());
		// then
		then(noteService.findByUser(user).size()).isZero();
	}

}
