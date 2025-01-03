package com.app.entity;


import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "answers")
public class Answer {
	
    private String questionId;
    private String selectedOption;
    private int questionNo;
    private String questionType;



    
}
