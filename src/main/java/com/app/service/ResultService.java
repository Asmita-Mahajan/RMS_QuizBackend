package com.app.service;

import com.app.entity.Answer;
import com.app.entity.AnswerSheet;
import com.app.entity.CandidateResult;
import com.app.entity.QuizSubmission;
import com.app.repository.AnswerSheetRepository;
import com.app.repository.CandidateResultRepository;
import com.app.repository.QuizSubmissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ResultService {

    @Autowired
    private QuizSubmissionRepository quizSubmissionRepository;

    @Autowired
    private AnswerSheetRepository answerSheetRepository;

    @Autowired
    private CandidateResultRepository candidateResultRepository;

    public CandidateResult saveResult(CandidateResult result) {
        return candidateResultRepository.save(result);
    }


    public int calculateResult(String candidateName, String testKey) {
        int score = 0; // Declare score as a local variable
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

    public List<CandidateResult> getAllResults() {
        List<QuizSubmission> submissions = quizSubmissionRepository.findAll();
        Map<String, CandidateResult> resultsMap = new HashMap<>();

        for (QuizSubmission submission : submissions) {
            String key = submission.getCandidateName() + "-" + submission.getTestKey();
            CandidateResult result = resultsMap.getOrDefault(key, new CandidateResult(submission.getCandidateName(), submission.getTestKey(), 0));

            for (Answer answer : submission.getAnswers()) {
                AnswerSheet answerSheet = answerSheetRepository.findByQuestionNo(answer.getQuestionNo());
                if (answerSheet != null && answerSheet.getCorrectOption().equals(answer.getSelectedOption())) {
                    result.setScore(result.getScore() + 1);
                }
            }

            resultsMap.put(key, result);
        }

        return new ArrayList<>(resultsMap.values());
    }
}
