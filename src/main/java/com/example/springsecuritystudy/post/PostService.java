package com.example.springsecuritystudy.post;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.springsecuritystudy.common.UserNotFoundException;
import com.example.springsecuritystudy.user.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class PostService {

	private final PostRepository postRepository;

	@Transactional(readOnly = true)
	public List<Post> findByUser(User user) {
		userNullCheck(user);
		if (Boolean.TRUE.equals(user.isAdmin())) {
			return postRepository.findByStatusOrderByIdDesc(PostStatus.Y);
		}
		return postRepository.findByUserAndStatusOrderByIdDesc(user, PostStatus.Y);
	}

	private static void userNullCheck(User user) {
		if (user == null) {
			throw new UserNotFoundException();
		}
	}

	public Post savePost(User user, String title, String content) {
		userNullCheck(user);
		return postRepository.save(new Post(title, content, user));
	}

	public void deletePost(User user, Long id) {
		userNullCheck(user);
		Post post = postRepository.findByIdAndUser(id, user);
		postRepository.delete(post);
	}

}
