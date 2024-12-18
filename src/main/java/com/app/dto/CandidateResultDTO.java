package com.app.dto;

public class CandidateResultDTO {
    private String candidateName;
    private String testKey;
    private int score;

    // Constructors, getters, and setters
    public CandidateResultDTO(String candidateName, String testKey, int score) {
        this.candidateName = candidateName;
        this.testKey = testKey;
        this.score = score;
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
}
