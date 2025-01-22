package com.app.repository;

import com.app.entity.CandidateResult;
import com.app.entity.TestStatus;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CandidateResultRepository extends MongoRepository<CandidateResult, String> {
    List<CandidateResult> findByCandidateNameAndTestKey(String candidateName, String testKey);

    // New method to save multiple candidates
    <S extends CandidateResult> List<S> saveAll(Iterable<S> entities);

    List<CandidateResult> findByTestStatus(TestStatus testStatus);


}