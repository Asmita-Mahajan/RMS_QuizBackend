package com.app.entity;


import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "answers")
public class Answer {
	
    private String questionId;
    private String selectedOption;
    private int questionNo; 
    // Getters and Setters
    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getSelectedOption() {
        return selectedOption;
    }

    public void setSelectedOption(String selectedOption) {
        this.selectedOption = selectedOption;
    }

	public int getQuestionNo() {
		return questionNo;
	}

	public void setQuestionNo(int questionNo) {
		this.questionNo = questionNo;
	}
    
}
