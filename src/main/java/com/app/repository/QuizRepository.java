package com.app.repository;


import com.app.entity.Quiz;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

// This interface is responsible for performing CRUD operations on the Quiz collection in MongoDB.
@Repository
public interface QuizRepository extends MongoRepository<Quiz, String> {
    // MongoRepository provides built-in methods like save(), findAll(), findById(), deleteById(), etc.
    
   
}
