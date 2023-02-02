package com.example.springsecuritystudy.post;

import java.util.List;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.springsecuritystudy.user.User;
import com.example.springsecuritystudy.user.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostService {

	private final UserRepository userRepository;
	private final PostRepository postRepository;

	@Transactional(readOnly = true)
	public List<Post> findByUserName(String username) {
		User user = getUser(username);
		return postRepository.findByUserAndStatus(user, PostStatus.Y);
	}

	public Post savePost(String username, String title, String content) {
		User user = getUser(username);
		return postRepository.save(new Post(title, content, user));
	}

	public void deletePost(String username, Long id) {
		User user = getUser(username);
		Post post = postRepository.findByIdAndUser(id, user);
		postRepository.delete(post);
	}

	private User getUser(String username) {
		return userRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("유저가 없습니다."));
	}

}
