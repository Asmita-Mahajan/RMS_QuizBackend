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
    private List<Answer> answers;


}