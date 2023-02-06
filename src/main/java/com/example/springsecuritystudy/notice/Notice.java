package com.example.springsecuritystudy.notice;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;

import com.example.springsecuritystudy.model.BaseTimeEntity;

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

	public Notice(String title, String content) {
		this.title = title;
		this.content = content;
	}
}
