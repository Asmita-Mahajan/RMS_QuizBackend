package com.app.repository;


import com.app.entity.Quiz;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface QuizRepository extends MongoRepository<Quiz, String> {

    
   
}
