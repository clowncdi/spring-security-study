package com.example.springsecuritystudy.note;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.springsecuritystudy.user.User;

public interface NoteRepository extends JpaRepository<Note, Long> {

	List<Note> findByUserOrderByIdDesc(User user);

	Note findByIdAndUser(Long id, User user);

}
