package com.app.service;

import java.io.IOException;
import java.util.List;

import com.app.entity.Answer;
import com.app.entity.UploadedQuiz;
import org.springframework.web.multipart.MultipartFile;

import com.app.entity.Quiz;
import com.app.entity.QuizSubmission;

public interface QuizService {
	 public List<Quiz> saveQuizzesFromExcel(MultipartFile file,String jobDescriptionId) throws IOException;
	 public List<Quiz> getAllQuizzes(String filename);
	 public void saveSubmission(QuizSubmission submission);

	 //answer service
	 public List<Answer> saveAnswers(List<Answer> answers);
	 //answersheet service
	 public void saveAnswerSheet(MultipartFile file) throws IOException;
	List<UploadedQuiz> getUploadedQuizzesByJobDescriptionName(String jobDescriptionName);
}
