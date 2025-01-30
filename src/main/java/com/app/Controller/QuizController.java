
package com.app.Controller;


import com.app.entity.CandidateResult;
import com.app.entity.Quiz;
import com.app.entity.QuizSubmission;

import com.app.entity.TestStatus;
import com.app.kafka.CandidateResultCompletedProducer;
import com.app.kafka.CandidateResultPendingProducer;
import com.app.service.QuizService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.app.service.ResultService;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/quiz")
public class QuizController {

    @Autowired
    private QuizService quizService;
    @Autowired
    private QuizService answerService;
    @Autowired
    private QuizService service;

    @Autowired
    private ResultService resultService;
    @Autowired
    private ResultService candidateResultService;

    @Autowired
    private CandidateResultPendingProducer producer;

    @Autowired
    private CandidateResultCompletedProducer producer2;


    private static final String TOPIC = "candidate_results";

    @PostMapping("/upload")
    public String uploadQuizFile(@RequestParam("file") MultipartFile file) {
        try {
            quizService.saveQuizzesFromExcel(file);
            return "File uploaded and quizzes saved successfully!";
        } catch (IOException e) {
            return "Error uploading file: " + e.getMessage();
        }
    }

    @GetMapping("/all")
    public List<Quiz> getAllQuizzes() {
        return quizService.getAllQuizzes();
    }
    
 // Endpoint to handle quiz submission
    @PostMapping("/submit")
    public ResponseEntity<List<CandidateResult>> submitQuiz(@RequestBody QuizSubmission submission) {
        submission.setTestStatus(TestStatus.COMPLETED);
        quizService.saveSubmission(submission);
        System.out.println("saveAllResults method called");
        List<CandidateResult> results = resultService.getAllResults();
        for (CandidateResult result : results) {
            result.setTestStatus(TestStatus.COMPLETED);
            candidateResultService.saveResult(result);
            producer2.sendMessage(result.toString());
            //producer2.sendMessage(result.toString());
            //kafkaProducerService.sendMessage(result.toString()); // Send result to
        }
        // Save the candidate's name, test key, and answers
        return ResponseEntity.ok(results);
    }

    @PostMapping("/answersupload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            service.saveAnswerSheet(file);
            return ResponseEntity.status(HttpStatus.OK).body("File uploaded and data saved successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload file: " + e.getMessage());
        }
    }

    @PostMapping("/resetquestions")
    public ResponseEntity<String> resetAndReassignQuestionNumbers() {
        quizService.resetAndReassignQuestionNumbers();
        return ResponseEntity.ok("Question numbers reset and reassigned starting from 1.");
    }

    @GetMapping("/validate")
    public ResponseEntity<Boolean> validateCandidate(@RequestParam String candidateName, @RequestParam String testKey) {
        boolean isValid = candidateResultService.isValidCandidate(candidateName, testKey);
        return ResponseEntity.ok(isValid);
    }
 
}
