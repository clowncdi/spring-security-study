package com.example.springsecuritystudy.notice;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;

import com.example.springsecuritystudy.model.BaseTimeEntity;
import com.example.springsecuritystudy.post.PostStatus;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notice extends BaseTimeEntity {

	@Id
	@GeneratedValue
	private Long id;
	private String title;
	@Lob
	private String content;
	@Enumerated(EnumType.STRING)
	private PostStatus status;

	public Notice(String title, String content) {
		this.title = title;
		this.content = content;
		this.status = PostStatus.Y;
	}
}
