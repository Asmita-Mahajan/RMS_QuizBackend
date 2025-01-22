
package com.app.Controller;

import com.app.entity.CandidateResult;
import com.app.entity.TestStatus;
import com.app.kafka.CandidateResultCompletedProducer;


import com.app.kafka.CandidateResultPendingProducer;
import com.app.service.ResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/results")
public class ResultController {

    @Autowired
    private ResultService resultService;
    @Autowired
    private ResultService candidateResultService;

  @Autowired
  private CandidateResultPendingProducer producer;

    @Autowired
    private CandidateResultCompletedProducer producer2;

    
    private static final String TOPIC = "candidate_results";

    @GetMapping("/{candidateName}/{testKey}")
    public ResponseEntity<Map<String, Object>> getResult(@PathVariable String candidateName, @PathVariable String testKey) {
        int commonScore = resultService.calculateResult(candidateName, testKey);
        Map<String, Integer> questionTypeScores = resultService.calculateResultByQuestionType(candidateName, testKey);
        Map<String, Long> totalQuestionsByType = resultService.calculateTotalQuestionsByType(candidateName, testKey);
        Map<String, Double> questionTypePercentages = resultService.calculateQuestionTypePercentages(questionTypeScores, totalQuestionsByType);

        Map<String, Object> response = Map.of(
                "commonScore", commonScore,
                "questionTypePercentages", questionTypePercentages
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/all")
    public ResponseEntity<Map<String, Object>> getAllCandidateResults() {
        List<CandidateResult> results = resultService.getAllResults();
        Map<String, Map<String, Double>> questionTypePercentages = results.stream()
                .collect(Collectors.toMap(
                        result -> result.getCandidateName() + "-" + result.getTestKey(),
                        CandidateResult::getQuestionTypePercentages
                ));

        Map<String, Object> response = Map.of(
                "results", results,
                "questionTypePercentages", questionTypePercentages
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping("/save")
    public ResponseEntity<List<CandidateResult>> saveAllResults() {
        System.out.println("saveAllResults method called");
        List<CandidateResult> results = resultService.getAllResults();
        for (CandidateResult result : results) {
            result.setTestStatus(TestStatus.COMPLETED);
            candidateResultService.saveResult(result);
            producer2.sendMessage(result.toString());
                //producer2.sendMessage(result.toString());
             //kafkaProducerService.sendMessage(result.toString()); // Send result to 
        }
        for (CandidateResult result : results) {
            System.out.println(result);
        }

        return ResponseEntity.ok(results);
    }
    
//    @PostMapping("/publish")
//    public String publishCandidateResult(@RequestBody CandidateResult candidateResult) {
//        producer.sendMessage(candidateResult);
//        return "Message published successfully!";
//    }


    @PostMapping("/publish")
    public String publishCandidateResults(@RequestBody List<CandidateResult> candidateResults) {
        producer.sendMessages(candidateResults);
        return "Messages published successfully!";
    }
}