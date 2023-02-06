package com.example.springsecuritystudy.post;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.springsecuritystudy.helper.TestConfig;
import com.example.springsecuritystudy.user.User;
import com.example.springsecuritystudy.user.UserService;

import static org.assertj.core.api.BDDAssertions.*;

class PostServiceTest extends TestConfig {

	@Autowired
	private PostService postService;
	@Autowired
	private UserService userService;

	@Test
	void findByUser_유저가_게시물조회() {
		//given
		User user = userService.signup("user", "user");
		postService.savePost(user, "title1", "content1");
		postService.savePost(user, "title2", "content2");
		//when
		List<Post> posts = postService.findByUser(user);
		//then
		then(posts.size()).isEqualTo(2);
		Post post1 = posts.get(0);
		Post post2 = posts.get(1);

		// post1 = title2
		then(post1.getUser().getUsername()).isEqualTo("user");
		then(post1.getTitle()).isEqualTo("title2");
		then(post1.getContent()).isEqualTo("content2");
		// post2 = title1
		then(post2.getUser().getUsername()).isEqualTo("user");
		then(post2.getTitle()).isEqualTo("title1");
		then(post2.getContent()).isEqualTo("content1");
	}

	@Test
	void findByUser_어드민이_조회() {
		// given
		User admin = userService.signupAdmin("admin", "admin");
		User user1 = userService.signup("user1", "user1");
		User user2 = userService.signup("user2", "user2");
		postService.savePost(user1, "title1", "content1");
		postService.savePost(user1, "title2", "content2");
		postService.savePost(user2, "title3", "content3");
		// when
		List<Post> posts = postService.findByUser(admin);
		// then
		then(posts.size()).isEqualTo(3);
		Post post1 = posts.get(0);
		Post post2 = posts.get(1);
		Post post3 = posts.get(2);

		// post1 = title3
		then(post1.getUser().getUsername()).isEqualTo("user2");
		then(post1.getTitle()).isEqualTo("title3");
		then(post1.getContent()).isEqualTo("content3");
		// post1 = title2
		then(post2.getUser().getUsername()).isEqualTo("user1");
		then(post2.getTitle()).isEqualTo("title2");
		then(post2.getContent()).isEqualTo("content2");
		// post1 = title1
		then(post3.getUser().getUsername()).isEqualTo("user1");
		then(post3.getTitle()).isEqualTo("title1");
		then(post3.getContent()).isEqualTo("content1");
	}

	@Test
	void savePost() {
		// given
		User user = userService.signup("user", "user");
		// when
		postService.savePost(user, "title", "content");
		// then
		then(postService.findByUser(user).size()).isEqualTo(1);
	}

	@Test
	void deletePost() {
		// given
		User user = userService.signup("user", "user");
		Post post = postService.savePost(user, "title", "content");
		// when
		postService.deletePost(user, post.getId());
		// then
		then(postService.findByUser(user).size()).isZero();
	}

}
