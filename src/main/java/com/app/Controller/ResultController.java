package com.app.Controller;


import com.app.dto.CandidateResultDTO;
import com.app.entity.QuizSubmission;
import com.app.service.CandidateResultService;
import com.app.service.ResultService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api/results")
public class ResultController {

    @Autowired
    private ResultService resultService;
    @Autowired
    private CandidateResultService candidateResultService;

    @GetMapping("/{candidateName}/{testKey}")
    public ResponseEntity<Integer> getResult(@PathVariable String candidateName, @PathVariable String testKey) {
        int score = resultService.calculateResult(candidateName, testKey);
        return ResponseEntity.ok(score);
    }
    
    @GetMapping("/all")
    public ResponseEntity<List<CandidateResultDTO>> getAllCandidateResults() {
        List<CandidateResultDTO> results = resultService.getAllResults();
        return ResponseEntity.ok(results);
    }
    
    @PostMapping("/save")
    public ResponseEntity<List<CandidateResultDTO>> saveAllResults() {
        List<CandidateResultDTO> results = resultService.getAllResults();
        for (CandidateResultDTO result : results) {
            candidateResultService.saveResult(result);
        }
        return ResponseEntity.ok(results);
    }
    
    
}