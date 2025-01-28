

package com.app.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@Document(collection = "CandidateResult")
public class CandidateResult {
    @Id
    private String id;
    private String candidateName;
    private String testKey;
    private int score;
    private TestStatus testStatus;
    private Map<String, Double> questionTypePercentages; // Only include this field

    // Constructor with parameters

    public CandidateResult() {
        // Initialize fields if necessary
    }

    public CandidateResult(String candidateName, String testKey) {
        this.candidateName = candidateName;
        this.testKey = testKey;
    }

 // Constructor to populate from message (assumed JSON string)
    public CandidateResult(String message) {
        // You can parse the message string here, for example, using Jackson
        // assuming 'message' is in JSON format and matches the class fields
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            CandidateResult candidateResult = objectMapper.readValue(message, CandidateResult.class);
            this.candidateName = candidateResult.candidateName;
            this.testKey = candidateResult.testKey;
            this.score = candidateResult.score;
            this.testStatus = candidateResult.testStatus;
            this.questionTypePercentages = candidateResult.questionTypePercentages;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


	@Override
    public String toString() {
        return "CandidateResult{" +
                "id='" + id + '\'' +
                ", candidateName='" + candidateName + '\'' +
                ", testKey='" + testKey + '\'' +
                ", score=" + score +
                ", testStatus=" + testStatus +
                ", questionTypePercentages=" + questionTypePercentages +
                '}';
    }


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getCandidateName() {
		return candidateName;
	}


	public void setCandidateName(String candidateName) {
		this.candidateName = candidateName;
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


	public Map<String, Double> getQuestionTypePercentages() {
		return questionTypePercentages;
	}


	public void setQuestionTypePercentages(Map<String, Double> questionTypePercentages) {
		this.questionTypePercentages = questionTypePercentages;
	}
}