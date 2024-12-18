package com.app.repository;


import org.springframework.data.mongodb.repository.MongoRepository;

import com.app.entity.Question;

public interface QuestionRepository extends MongoRepository<Question, String> {
}
