
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
    private ResultService resultService;

    @Autowired
    private CandidateResultCompletedProducer producer2;

    @Autowired
    private JobDescriptionService jobDescriptionService;

//upload quiz file wrt jobdescription
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

    @GetMapping("/allquize")
    public List<Quiz> getAllQuizzes(@RequestParam(required = false) String filename) {
        return quizService.getAllQuizzes(filename);
    }

 // Endpoint to handle quiz submission and calculate result
    @PostMapping("/submit")
    public ResponseEntity<List<CandidateResult>> submitQuiz(@RequestBody QuizSubmission submission) {
        //to save Quiz submission into database
        submission.setTestStatus(TestStatus.COMPLETED);
        quizService.saveSubmission(submission);
        System.out.println("saveAllResults method called");
        //to caluclate result
        List<CandidateResult> results = resultService.calucalteAllResults();
        for (CandidateResult result : results) {
            resultService.saveResult(result);
            producer2.sendMessage(result.toString());

        }

        return ResponseEntity.ok(results);
    }
//to upload answer excel sheet
    @PostMapping("/answersupload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            quizService.saveAnswerSheet(file);
            return ResponseEntity.status(HttpStatus.OK).body("File uploaded and data saved successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload file: " + e.getMessage());
        }
    }


// to validate candidate email and testkey
    @GetMapping("/validate")
    public ResponseEntity<Boolean> validateCandidate(@RequestParam String email) {
        boolean isValid = resultService.isValidCandidate(email);
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


    @GetMapping("/allcompletedcandidate")
    public ResponseEntity<List<CandidateResult>> getAllCandidateResults() {
        List<CandidateResult> completedResults = resultService.getAllCompletedResults();
        System.out.println("Number of completed results retrieved: " + completedResults.size());

        return ResponseEntity.ok(completedResults);
    }



//    @PostMapping("/publish")
//    public String publishCandidateResults(@RequestBody List<CandidateResult> candidateResults) {
//        producer.sendMessages(candidateResults);
//        return "Messages published successfully!";
//    }


}
