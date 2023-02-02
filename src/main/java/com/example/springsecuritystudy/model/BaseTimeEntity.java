package com.example.springsecuritystudy.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.ToString;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
@ToString(callSuper = true)
public abstract class BaseTimeEntity {

	@JsonFormat(timezone = "Asia/Seoul")
	@CreatedDate
	@Column(nullable = false, updatable = false)
	private LocalDateTime createdAt;

	@JsonFormat(timezone = "Asia/Seoul")
	@LastModifiedDate
	private LocalDateTime updatedAt;
}
