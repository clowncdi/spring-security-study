package com.example.springsecuritystudy.post;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.springsecuritystudy.user.User;

public interface PostRepository extends JpaRepository<Post, Long> {

	List<Post> findByUserAndStatus(User user, PostStatus status);

	Post findByIdAndUser(Long id, User user);
}
