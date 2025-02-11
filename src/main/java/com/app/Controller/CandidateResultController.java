package com.app.Controller;

import com.app.dto.ScheduleCandidatesRequest;
import com.app.entity.CandidateResult;
import com.app.service.CandidateResultService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/candidates")
public class CandidateResultController {

    private final CandidateResultService candidateResultService;

    public CandidateResultController(CandidateResultService candidateResultService) {
        this.candidateResultService = candidateResultService;
    }

    // Get all candidates
    @GetMapping
    public ResponseEntity<List<CandidateResult>> getAllCandidates() {
        return ResponseEntity.ok(candidateResultService.getAllCandidateResults());
    }

    @PostMapping("/schedule")
    public ResponseEntity<String> scheduleCandidates(@RequestBody ScheduleCandidatesRequest request) {
        candidateResultService.scheduleCandidates(request);
        return ResponseEntity.ok("Candidates scheduled successfully.");
    }

}
