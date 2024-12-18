
//


package com.app.service;

import com.app.entity.Quiz;
import com.app.entity.QuizSubmission;
import com.app.repository.QuizRepository;
import com.app.repository.QuizSubmissionRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

@Service
public class QuizServiceImpl implements QuizService {

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private SequenceGeneratorService sequenceGeneratorService;

    @Autowired
    private QuizSubmissionRepository quizSubmissionRepository;

    public List<Quiz> saveQuizzesFromExcel(MultipartFile file) throws IOException {
        List<Quiz> quizzes = new ArrayList<>();

        InputStream inputStream = file.getInputStream();
        Workbook workbook = new XSSFWorkbook(inputStream);
        Sheet sheet = workbook.getSheetAt(0);

        Iterator<Row> rows = sheet.iterator();

        while (rows.hasNext()) {
            Row row = rows.next();
            if (row.getRowNum() == 0) continue; // Skip the header row

            String question = row.getCell(0).getStringCellValue();
            String optionA = row.getCell(1).getStringCellValue();
            String optionB = row.getCell(2).getStringCellValue();
            String optionC = row.getCell(3).getStringCellValue();
            String optionD = row.getCell(4).getStringCellValue();

            Quiz quiz = new Quiz(question, optionA, optionB, optionC, optionD);
            quiz.setQuestionNo(sequenceGeneratorService.generateSequence(Quiz.SEQUENCE_NAME));
            quiz.setSet("A"); // Default set to "A"
            quizzes.add(quiz);
        }

        quizRepository.saveAll(quizzes);
        return quizzes;
    }

//    public List<Quiz> getAllQuizzes() {
//        return quizRepository.findAll();
//    }
    public List<Quiz> getAllQuizzes() {
        try {
            List<Quiz> quizzes = quizRepository.findAll(); // Fetch quizzes from the database
            Collections.shuffle(quizzes); // Shuffle the list of quizzes
            return quizzes;
        } catch (Exception e) {
            // Log the exception (you can use a logging framework like SLF4J)
            System.err.println("Error fetching quizzes: " + e.getMessage());
            throw new RuntimeException("Failed to fetch quizzes", e);
        }
    }
    public void saveSubmission(QuizSubmission submission) {
        quizSubmissionRepository.save(submission);  // Save the submission to the database
    }
}

