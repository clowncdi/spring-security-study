package com.example.springsecuritystudy.post;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.example.springsecuritystudy.model.BaseTimeEntity;
import com.example.springsecuritystudy.user.User;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends BaseTimeEntity {

	@Id
	@GeneratedValue
	private Long id;
	private String title;
	@Lob
	private String content;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USER_ID")
	private User user;

	@Builder
	public Post(String title, String content, User user) {
		this.title = title;
		this.content = content;
		this.user = user;
	}

	public void updatePost(String title, String content) {
		this.title = title;
		this.content = content;
	}
}
