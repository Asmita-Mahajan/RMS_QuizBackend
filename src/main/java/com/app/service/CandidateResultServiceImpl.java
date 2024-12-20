package com.app.service;


import com.app.dto.CandidateResultDTO;
import com.app.repository.CandidateResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CandidateResultServiceImpl implements CandidateResultService{

    @Autowired
    private CandidateResultRepository candidateResultRepository;

    public CandidateResultDTO saveResult(CandidateResultDTO result) {
        return candidateResultRepository.save(result);
    }
}
