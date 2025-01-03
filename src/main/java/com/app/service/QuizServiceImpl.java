
package com.app.service;

import com.app.entity.*;
import com.app.repository.AnswerRepository;
import com.app.repository.AnswerSheetRepository;
import com.app.repository.QuizRepository;
import com.app.repository.QuizSubmissionRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Service
public class QuizServiceImpl implements QuizService {

    @Autowired
    private QuizRepository quizRepository;
    @Autowired
    private MongoOperations mongoOperations;
    @Autowired
    private SequenceGeneratorService sequenceGeneratorService;

    @Autowired
    private QuizSubmissionRepository quizSubmissionRepository;

    @Autowired
    private AnswerSheetRepository repository;

    @Autowired
    private AnswerRepository answerRepository;

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
            String questionType = row.getCell(5).getStringCellValue();
            Quiz quiz = new Quiz(question, optionA, optionB, optionC, optionD,questionType);
            quiz.setQuestionNo(sequenceGeneratorService.generateSequence(Quiz.SEQUENCE_NAME));
            quiz.setSet("A"); // Default set to "A"
            quizzes.add(quiz);
        }

        quizRepository.saveAll(quizzes);
        return quizzes;
    }

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


    public void resetAndReassignQuestionNumbers() {
        // Step 1: Reset all questionNo fields to 1
        Query query = new Query(); // This query matches all documents
        Update update = new Update().set("questionNo", 1); // Set questionNo to 1
        mongoOperations.updateMulti(query, update, Quiz.class);

        // Step 2: Retrieve all quizzes and reassign questionNo in sequence
        List<Quiz> quizzes = mongoOperations.find(query, Quiz.class);
        int questionNumber = 1;
        for (Quiz quiz : quizzes) {
            quiz.setQuestionNo(questionNumber++);
            mongoOperations.save(quiz);
        }
    }
    //answer by candidate

    public List<Answer> saveAnswers(List<Answer> answers) {
        return answerRepository.saveAll(answers);
    }

    //answersheet

    public void saveAnswerSheet(MultipartFile file) throws IOException {
        List<AnswerSheet> answerSheets = parseExcelFile(file.getInputStream());
        repository.saveAll(answerSheets);
    }
    private List<AnswerSheet> parseExcelFile(InputStream is) throws IOException {
        List<AnswerSheet> answerSheets = new ArrayList<>();
        Workbook workbook = new XSSFWorkbook(is);
        Sheet sheet = workbook.getSheetAt(0);

        for (Row row : sheet) {
            // Skip the header row
            if (row.getRowNum() == 0) {
                continue;
            }

            AnswerSheet answerSheet = new AnswerSheet();

            // Parse questionNo
            Cell questionNoCell = row.getCell(0);
            if (questionNoCell != null) {
                if (questionNoCell.getCellType() == CellType.NUMERIC) {
                    answerSheet.setQuestionNo((int) questionNoCell.getNumericCellValue());
                } else if (questionNoCell.getCellType() == CellType.STRING) {
                    try {
                        answerSheet.setQuestionNo(Integer.parseInt(questionNoCell.getStringCellValue()));
                    } catch (NumberFormatException e) {
                        throw new IOException("Invalid format for question number: " + questionNoCell.getStringCellValue(), e);
                    }
                }
            }

            // Parse correctOption
            Cell correctOptionCell = row.getCell(1);
            if (correctOptionCell != null && correctOptionCell.getCellType() == CellType.STRING) {
                answerSheet.setCorrectOption(correctOptionCell.getStringCellValue());
            }

            // Parse questionType
            Cell questionTypeCell = row.getCell(2); // Assuming questionType is in the third column
            if (questionTypeCell != null && questionTypeCell.getCellType() == CellType.STRING) {
                answerSheet.setQuestionType(questionTypeCell.getStringCellValue());
            }

            answerSheets.add(answerSheet);
        }

        workbook.close();
        return answerSheets;
    }


}

