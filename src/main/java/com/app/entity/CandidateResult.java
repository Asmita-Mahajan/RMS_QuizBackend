package com.app.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "candidate_results")
public class CandidateResult {
    @Id
    private String id;
    private String candidateName;
    private String testKey;
    private int score;
    private TestStatus testStatus;

    // Constructors, getters, and setters
    public CandidateResult(String candidateName, String testKey, int score) {
        this.candidateName = candidateName;
        this.testKey = testKey;
        this.score = score;
        this.testStatus = TestStatus.COMPLETED; // Set default testStatus to QUIZ
    }
    @Override
    public String toString() {
        return "CandidateResult{" +
                "candidateName='" + candidateName + '\'' +
                ", testKey='" + testKey + '\'' +
                ", score=" + score +
                ", testStatus=" + testStatus + // Include testStatus in toString
                '}';
    }

}
