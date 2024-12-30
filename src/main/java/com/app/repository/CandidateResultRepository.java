package com.app.repository;




import com.app.entity.CandidateResult;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CandidateResultRepository extends MongoRepository<CandidateResult, String> {
    // Custom query methods if needed
}