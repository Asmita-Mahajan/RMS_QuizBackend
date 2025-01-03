package com.app.entity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import org.springframework.data.annotation.Transient;


@Getter
@Setter
	@Document(collection = "quizzes")
	public class Quiz {
		@Transient public static final String SEQUENCE_NAME = "questions_sequence";
		@Id private String id; 
		private int questionNo; 
		private String set = "A";
		// Default set to "A" 
		private String question; 
		private String optionA; 
		private String optionB; 
		private String optionC; 
		private String optionD;
		private String questionType;

	    // Getters and setters...
	    
	    

	    public Quiz(String question, String optionA, String optionB, String optionC, String optionD, String questionType) {
	        this.question = question;
	        this.optionA = optionA;
	        this.optionB = optionB;
	        this.optionC = optionC;
	        this.optionD = optionD;
			this.questionType=questionType;
	    }


		
		
	}

	

