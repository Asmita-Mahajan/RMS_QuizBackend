package com.app.repository;


import com.app.entity.JobDescription;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface JobDescriptionRepository extends MongoRepository<JobDescription, String> {
    String findByFilename(String filename);
    // You can add custom query methods here if needed
}