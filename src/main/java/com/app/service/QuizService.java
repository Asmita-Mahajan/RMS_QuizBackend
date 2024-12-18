package com.app.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.app.entity.Quiz;
import com.app.entity.QuizSubmission;

public interface QuizService {
	 public List<Quiz> saveQuizzesFromExcel(MultipartFile file) throws IOException;
	 public List<Quiz> getAllQuizzes();
	 public void saveSubmission(QuizSubmission submission);
	 
}
