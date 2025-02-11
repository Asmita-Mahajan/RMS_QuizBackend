package com.app.repository;

import com.app.entity.JobDescription;
import com.app.entity.UploadedQuiz;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface UploadedQuizRepository extends MongoRepository<UploadedQuiz, String> {
    // Additional query methods can be defined here if needed
    List<UploadedQuiz> findByJobDescription(String jobDescriptionId);
}
