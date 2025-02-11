package com.app.repository;



import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.app.entity.QuizSubmission;

public interface QuizSubmissionRepository extends MongoRepository<QuizSubmission, String> {
	
	List<QuizSubmission> findByEmailAndTestKey(String email, String testKey);
}



