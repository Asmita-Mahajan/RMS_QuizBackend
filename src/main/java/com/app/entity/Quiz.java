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

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public int getQuestionNo() {
			return questionNo;
		}

		public void setQuestionNo(int questionNo) {
			this.questionNo = questionNo;
		}

		public String getSet() {
			return set;
		}

		public void setSet(String set) {
			this.set = set;
		}

		public String getQuestion() {
			return question;
		}

		public void setQuestion(String question) {
			this.question = question;
		}

		public String getOptionA() {
			return optionA;
		}

		public void setOptionA(String optionA) {
			this.optionA = optionA;
		}

		public String getOptionB() {
			return optionB;
		}

		public void setOptionB(String optionB) {
			this.optionB = optionB;
		}

		public String getOptionC() {
			return optionC;
		}

		public void setOptionC(String optionC) {
			this.optionC = optionC;
		}

		public String getOptionD() {
			return optionD;
		}

		public void setOptionD(String optionD) {
			this.optionD = optionD;
		}

		public String getQuestionType() {
			return questionType;
		}

		public void setQuestionType(String questionType) {
			this.questionType = questionType;
		}

		public static String getSequenceName() {
			return SEQUENCE_NAME;
		}


		
		
	}

	

