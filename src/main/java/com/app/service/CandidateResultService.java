package com.app.service;

import com.app.dto.ScheduleCandidatesRequest;
import com.app.entity.CandidateResult;
import com.app.entity.CurrentStatus;
import com.app.repository.CandidateResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CandidateResultService {

    @Autowired
    private CandidateResultRepository candidateResultRepository;

    // Get all candidates
    public List<CandidateResult> getAllCandidateResults() {
        return candidateResultRepository.findAll();
    }

    public void scheduleCandidates(ScheduleCandidatesRequest request) {
        List<String> emails = request.getEmails();
        String testKey = request.getTestKey();

        for (String email : emails) {
            // Find the CandidateResults by email
            List<CandidateResult> candidateResults = candidateResultRepository.findByEmail(email);

            // Check if any CandidateResults were found
            if (candidateResults != null && !candidateResults.isEmpty()) {
                for (CandidateResult candidateResult : candidateResults) {
                    // Update the testKey and currentStatus
                    candidateResult.setTestKey(testKey);
                    candidateResult.setCurrentStatus(CurrentStatus.MCQ_SCHEDULED);
                    candidateResultRepository.save(candidateResult); // Save the updated candidate result
                }
            }
        }
    }
}
