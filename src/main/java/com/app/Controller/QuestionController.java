package com.app.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.entity.Question;
import com.app.service.QuestionService;

import java.util.List;

@RestController
@RequestMapping("/api/question")
public class QuestionController {
	 @Autowired
	    private QuestionService questionService;

	    @GetMapping("/getquestions")
	    public List<Question> getAllQuestions() {
	        return questionService.getAllQuestions();
}
}