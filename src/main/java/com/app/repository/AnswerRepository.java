package com.app.repository;



import org.springframework.data.mongodb.repository.MongoRepository;

import com.app.entity.Answer;

public interface AnswerRepository extends MongoRepository<Answer, String> {
}
