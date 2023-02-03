package com.example.springsecuritystudy.notice;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class NoticeService {

	private final NoticeRepository noticeRepository;

	@Transactional(readOnly = true)
	public List<Notice> findAll() {
		return noticeRepository.findAll();
	}

	public Notice saveNotice(String title, String content) {
		return noticeRepository.save(new Notice(title, content));
	}

	public void deleteNotice(Long id) {
		noticeRepository.findById(id).ifPresent(noticeRepository::delete);
	}
}
