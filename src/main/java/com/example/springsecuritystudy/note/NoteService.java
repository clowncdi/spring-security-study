package com.example.springsecuritystudy.note;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.springsecuritystudy.common.UserNotFoundException;
import com.example.springsecuritystudy.user.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class NoteService {

	private final NoteRepository noteRepository;

	@Transactional(readOnly = true)
	public List<Note> findByUser(User user) {
		userNullCheck(user);
		if (Boolean.TRUE.equals(user.isAdmin())) {
			return noteRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
		}
		return noteRepository.findByUserOrderByIdDesc(user);
	}

	private static void userNullCheck(User user) {
		if (user == null) {
			throw new UserNotFoundException();
		}
	}

	public Note saveNote(User user, String title, String content) {
		userNullCheck(user);
		return noteRepository.save(new Note(title, content, user));
	}

	public void deleteNote(User user, Long id) {
		userNullCheck(user);
		Note note = noteRepository.findByIdAndUser(id, user);
		noteRepository.delete(note);
	}

}
