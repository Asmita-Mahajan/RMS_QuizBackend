package com.app.entity;


import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;


@Getter
@Setter
@Document(collection = "quiz_submissions")
public class QuizSubmission {

    @Id
    private String id;
    //private int questionNo; 
    private String candidateName;
    private String testKey;
    private TestStatus testStatus;
    private List<Answer> answers;
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
	public TestStatus getTestStatus() {
		return testStatus;
	}
	public void setTestStatus(TestStatus testStatus) {
		this.testStatus = testStatus;
	}
	public List<Answer> getAnswers() {
		return answers;
	}
	public void setAnswers(List<Answer> answers) {
		this.answers = answers;
	}
    
    


}