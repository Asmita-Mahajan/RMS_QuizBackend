package com.app.repository;




import com.app.dto.CandidateResultDTO;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CandidateResultRepository extends MongoRepository<CandidateResultDTO, String> {
    // Custom query methods if needed
}