

package com.app.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

@Data
@AllArgsConstructor
@Builder
@Document(collection = "CandidateResult")
public class CandidateResult {
    @Id
    private String id;
    private String email;
//	private JobRole jobrole;
    private String testKey;
    private int score;
    private TestStatus testStatus;
	private String jdfilename;
	private CurrentStatus currentStatus;

    private Map<String, Double> questionTypePercentages; // Only include this field

    // Constructor with parameters

    public CandidateResult() {
        // Initialize fields if necessary
    }

    public CandidateResult(String email, String testKey) {
        this.email = email;
        this.testKey = testKey;
    }

//  Constructor to populate from message (assumed JSON string)
    public CandidateResult(String message) {
        // You can parse the message string here, for example, using Jackson
        // assuming 'message' is in JSON format and matches the class fields
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            CandidateResult candidateResult = objectMapper.readValue(message, CandidateResult.class);
            this.email = candidateResult.email;
//            this.jobrole=candidateResult.jobrole;
//            this.score = candidateResult.score;
			this.testKey = "null";
			this.jdfilename = candidateResult.jdfilename;
			this.testStatus = TestStatus.PENDING;
			this.currentStatus = candidateResult.currentStatus;
			this.questionTypePercentages = candidateResult.questionTypePercentages;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTestKey() {
        return testKey;
    }

    public void setTestKey(String testKey) {
        this.testKey = testKey;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public TestStatus getTestStatus() {
        return testStatus;
    }

    public void setTestStatus(TestStatus testStatus) {
        this.testStatus = testStatus;
    }

    public String getJdfilename() {
        return jdfilename;
    }

    public void setJdfilename(String jdfilename) {
        this.jdfilename = jdfilename;
    }

    public CurrentStatus getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(CurrentStatus currentStatus) {
        this.currentStatus = currentStatus;
    }

    public Map<String, Double> getQuestionTypePercentages() {
        return questionTypePercentages;
    }

    public void setQuestionTypePercentages(Map<String, Double> questionTypePercentages) {
        this.questionTypePercentages = questionTypePercentages;
    }
}