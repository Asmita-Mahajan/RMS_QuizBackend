package com.app.entity;



import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;

@Document(collection = "uploaded_quizzes")
public class UploadedQuiz {

    @Id
    private String id;

    // Use DBRef to reference JobDescription
    private String jobDescription;

    private String fileName; // Store the name of the uploaded file

    // Constructors, getters, and setters
    public UploadedQuiz() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
