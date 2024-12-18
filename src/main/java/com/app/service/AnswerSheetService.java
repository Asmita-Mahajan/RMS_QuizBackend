package com.app.service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

public interface AnswerSheetService {
	public void saveAnswerSheet(MultipartFile file) throws IOException;
}
