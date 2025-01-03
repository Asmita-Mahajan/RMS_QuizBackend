package com.app.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

@Getter
@Setter
@Document(collection = "candidateResults")
public class CandidateResult {
    @Id
    private String id;
    private String candidateName;
    private String testKey;
    private int score;
    private String testStatus;
    private Map<String, Double> questionTypePercentages; // Only include this field

    // Default constructor
    public CandidateResult() {
    }

    // Constructor with parameters
    public CandidateResult(String candidateName, String testKey, int score, Map<String, Double> questionTypePercentages) {
        this.candidateName = candidateName;
        this.testKey = testKey;
        this.score = score;
        this.questionTypePercentages = questionTypePercentages;
    }

    public CandidateResult(String candidateName, String testKey) {
        this.candidateName = candidateName;
        this.testKey = testKey;
    }
}