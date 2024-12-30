package com.app.Controller;


import com.app.entity.CandidateResult;


import com.app.service.KafkaProducerService;
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
    private ResultService candidateResultService;

    @Autowired
    private KafkaProducerService kafkaProducerService;
    private static final String TOPIC = "candidate_results";

    @GetMapping("/{candidateName}/{testKey}")
    public ResponseEntity<Integer> getResult(@PathVariable String candidateName, @PathVariable String testKey) {
        int score = resultService.calculateResult(candidateName, testKey);
        return ResponseEntity.ok(score);
    }
    
    @GetMapping("/all")
    public ResponseEntity<List<CandidateResult>> getAllCandidateResults() {
        List<CandidateResult> results = resultService.getAllResults();
        return ResponseEntity.ok(results);
    }


//    @PostMapping("/save")
//    public ResponseEntity<List<CandidateResult>> saveAllResults() {
//        List<CandidateResult> results = resultService.getAllResults();
//        for (CandidateResult result : results) {
//            candidateResultService.saveResult(result);
//        }
//        return ResponseEntity.ok(results);
//    }

//@PostMapping("/save")
//public ResponseEntity<List<CandidateResult>> saveAllResults() {
//    List<CandidateResult> results = resultService.getAllResults();
//    for (CandidateResult result : results) {
//        candidateResultService.saveResult(result);
//        kafkaProducerService.sendMessage(result.toString()); // Send result to Kafka
//    }
//    return ResponseEntity.ok(results);
//}
@PostMapping("/save")
public ResponseEntity<List<CandidateResult>> saveAllResults() {
    List<CandidateResult> results = resultService.getAllResults();
    for (CandidateResult result : results) {
        candidateResultService.saveResult(result);
        kafkaProducerService.sendMessage(result.toString()); // Send result to Kafka
    }
    return ResponseEntity.ok(results);
}

}