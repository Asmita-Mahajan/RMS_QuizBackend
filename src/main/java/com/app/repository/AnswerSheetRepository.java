package com.app.repository;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.app.entity.AnswerSheet;

public interface AnswerSheetRepository extends MongoRepository<AnswerSheet, String> {
	 AnswerSheet findByQuestionNo(int questionNo);
}