
package com.app.Controller;


import com.app.entity.*;

import com.app.kafka.CandidateResultCompletedProducer;
//import com.app.kafka.CandidateResultPendingProducer;
import com.app.repository.JobDescriptionRepository;
import com.app.service.JobDescriptionService;
import com.app.service.QuizService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
    private CandidateResultCompletedProducer producer2;

    @Autowired
    private JobDescriptionService jobDescriptionService;




    private static final String TOPIC = "candidate_results";

    @PostMapping("/upload")
    public String uploadQuizFile(@RequestPart("file") MultipartFile file,
                                 @RequestParam("jobDescriptionId") String jobDescriptionId) {
        try {
            quizService.saveQuizzesFromExcel(file, jobDescriptionId);
            return "File uploaded and quizzes saved successfully!";
        } catch (IOException e) {
            return "Error uploading file: " + e.getMessage();
        }
    }

    @GetMapping("/all")
    public List<Quiz> getAllQuizzes(@RequestParam(required = false) String filename) {
        return quizService.getAllQuizzes(filename);
    }
    
 // Endpoint to handle quiz submission
    @PostMapping("/submit")
    public ResponseEntity<List<CandidateResult>> submitQuiz(@RequestBody QuizSubmission submission) {
        submission.setTestStatus(TestStatus.COMPLETED);
        quizService.saveSubmission(submission);
        System.out.println("saveAllResults method called");
        List<CandidateResult> results = resultService.getAllResults();
        for (CandidateResult result : results) {
//            result.setTestStatus(TestStatus.COMPLETED);
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
    public ResponseEntity<Boolean> validateCandidate(@RequestParam String email) {
        boolean isValid = candidateResultService.isValidCandidate(email);
        return ResponseEntity.ok(isValid);
    }

    @GetMapping(value = "/filenames", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<String>> getAllJobDescriptionFilenames() {
        List<String> filenames = jobDescriptionService.getAllJobDescriptionFilenames();
        return ResponseEntity.ok(filenames);
    }
    //to find the uploaded quizzes by job description
    @GetMapping("/uploaded/by-name/{jobDescriptionName}")
    public ResponseEntity<List<UploadedQuiz>> getUploadedQuizzesByJobDescriptionName(
            @PathVariable String jobDescriptionName) {
        try {
            List<UploadedQuiz> uploadedQuizzes = quizService.getUploadedQuizzesByJobDescriptionName(jobDescriptionName);
            if (uploadedQuizzes.isEmpty()) {
                return ResponseEntity.noContent().build(); // Return 204 if no quizzes found
            }
            return ResponseEntity.ok(uploadedQuizzes);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build(); // Return 404 if JobDescription not found
        }
    }

}
