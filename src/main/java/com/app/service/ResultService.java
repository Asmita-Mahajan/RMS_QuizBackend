

package com.app.service;

import com.app.entity.Answer;
import com.app.entity.AnswerSheet;
import com.app.entity.CandidateResult;
import com.app.entity.QuizSubmission;
import com.app.entity.TestStatus;
import com.app.repository.AnswerSheetRepository;
import com.app.repository.CandidateResultRepository;
import com.app.repository.QuizSubmissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ResultService {

    @Autowired
    private QuizSubmissionRepository quizSubmissionRepository;

    @Autowired
    private AnswerSheetRepository answerSheetRepository;

    @Autowired
    private CandidateResultRepository candidateResultRepository;

    public CandidateResult saveResult(CandidateResult result) {
        List<CandidateResult> existingResults = candidateResultRepository.findByCandidateNameAndTestKey(result.getCandidateName(), result.getTestKey());
        if (!existingResults.isEmpty()) {
            for (CandidateResult existingResult : existingResults) {
                existingResult.setScore(result.getScore());
                existingResult.setTestStatus(TestStatus.COMPLETED);
                existingResult.setQuestionTypePercentages(result.getQuestionTypePercentages());
                candidateResultRepository.save(existingResult);
            }
            return existingResults.get(0); // Return the first updated result
        } else {
            System.out.println("Saving new result: " + result);
            return candidateResultRepository.save(result);
        }
    }

    public List<CandidateResult> getAllCompletedResults() {
        return candidateResultRepository.findByTestStatus(TestStatus.COMPLETED);
    }

    public int calculateResult(String candidateName, String testKey) {
        int score = 0;
        List<QuizSubmission> submissions = quizSubmissionRepository.findByCandidateNameAndTestKey(candidateName, testKey);

        for (QuizSubmission submission : submissions) {
            for (Answer answer : submission.getAnswers()) {
                AnswerSheet answerSheet = answerSheetRepository.findByQuestionNo(answer.getQuestionNo());
                if (answerSheet != null && answerSheet.getCorrectOption().equals(answer.getSelectedOption())) {
                    score++;
                }
            }
        }

        return score;
    }

    public CandidateResult calculateAndSaveResult(String candidateName, String testKey, List<Answer> answers) {
        // Calculate the score
        int score = calculateScore(answers);

        // Calculate question type percentages
        Map<String, Integer> questionTypeScores = calculateResultByQuestionType(candidateName, testKey);
        Map<String, Long> totalQuestionsByType = calculateTotalQuestionsByType(candidateName, testKey);
        Map<String, Double> questionTypePercentages = calculateQuestionTypePercentages(questionTypeScores, totalQuestionsByType);

        // Create a new CandidateResult object
        CandidateResult candidateResult = new CandidateResult();
        candidateResult.setCandidateName(candidateName);
        candidateResult.setTestKey(testKey);
        candidateResult.setScore(score);
        candidateResult.setTestStatus(TestStatus.COMPLETED);
        candidateResult.setQuestionTypePercentages(questionTypePercentages);

        // Save the result to the database
        return candidateResultRepository.save(candidateResult);
    }

    // Helper method to calculate score based on answers
    private int calculateScore(List<Answer> answers) {
        int score = 0;
        for (Answer answer : answers) {
            AnswerSheet answerSheet = answerSheetRepository.findByQuestionNo(answer.getQuestionNo());
            if (answerSheet != null && answerSheet.getCorrectOption().equals(answer.getSelectedOption())) {
                score++;
            }
        }
        return score;
    }

    public Map<String, Integer> calculateResultByQuestionType(String candidateName, String testKey) {
        List<QuizSubmission> submissions = quizSubmissionRepository.findByCandidateNameAndTestKey(candidateName, testKey);

        return submissions.stream()
                .flatMap(submission -> submission.getAnswers().stream())
                .filter(answer -> answer.getQuestionType() != null) // Filter out null question types
                .collect(Collectors.groupingBy(Answer::getQuestionType, Collectors.summingInt(answer -> {
                    AnswerSheet answerSheet = answerSheetRepository.findByQuestionNo(answer.getQuestionNo());
                    return (answerSheet != null && answerSheet.getCorrectOption().equals(answer.getSelectedOption())) ? 1 : 0;
                })));
    }

    public Map<String, Long> calculateTotalQuestionsByType(String candidateName, String testKey) {
        List<QuizSubmission> submissions = quizSubmissionRepository.findByCandidateNameAndTestKey(candidateName, testKey);

        return submissions.stream()
                .flatMap(submission -> submission.getAnswers().stream())
                .filter(answer -> answer.getQuestionType() != null) // Filter out null question types
                .collect(Collectors.groupingBy(Answer::getQuestionType, Collectors.counting()));
    }

    public Map<String, Double> calculateQuestionTypePercentages(Map<String, Integer> questionTypeScores, Map<String, Long> totalQuestionsByType) {
        Map<String, Double> percentages = new HashMap<>();
        for (String questionType : questionTypeScores.keySet()) {
            int score = questionTypeScores.get(questionType);
            long totalQuestions = totalQuestionsByType.getOrDefault(questionType, 1L); // Avoid division by zero
            double percentage = (double) score / totalQuestions * 100;
            percentages.put(questionType, percentage);
        }
        return percentages;
    }

    public boolean isValidCandidate(String candidateName, String testKey) {
        // Implement the logic to check if the candidate exists in the database
        List<CandidateResult> results = candidateResultRepository.findByCandidateNameAndTestKey(candidateName, testKey);
        return !results.isEmpty(); // Return true if the candidate exists
    }

    public List<CandidateResult> getAllResults() {
        List<QuizSubmission> submissions = quizSubmissionRepository.findAll();
        Map<String, CandidateResult> resultsMap = new HashMap<>();

        for (QuizSubmission submission : submissions) {
            String key = submission.getCandidateName() + "-" + submission.getTestKey();
            CandidateResult result = resultsMap.getOrDefault(key, new CandidateResult(submission.getCandidateName(), submission.getTestKey()));

            for (Answer answer : submission.getAnswers()) {
                AnswerSheet answerSheet = answerSheetRepository.findByQuestionNo(answer.getQuestionNo());
                if (answerSheet != null && answerSheet.getCorrectOption().equals(answer.getSelectedOption())) {
                    result.setScore(result.getScore() + 1);
                }
            }

            Map<String, Integer> questionTypeScores = calculateResultByQuestionType(submission.getCandidateName(), submission.getTestKey());
            Map<String, Long> totalQuestionsByType = calculateTotalQuestionsByType(submission.getCandidateName(), submission.getTestKey());
            Map<String, Double> questionTypePercentages = calculateQuestionTypePercentages(questionTypeScores, totalQuestionsByType);

            result.setQuestionTypePercentages(questionTypePercentages);
            resultsMap.put(key, result);
        }

        return new ArrayList<>(resultsMap.values());
    }
}