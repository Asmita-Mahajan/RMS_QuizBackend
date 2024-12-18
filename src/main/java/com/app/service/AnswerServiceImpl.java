package com.app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.entity.Answer;
import com.app.repository.AnswerRepository;

@Service
public class AnswerServiceImpl implements AnswerService {
	@Autowired
    private AnswerRepository answerRepository;

    public List<Answer> saveAnswers(List<Answer> answers) {
        return answerRepository.saveAll(answers);
    }
}
