package com.app.service;

 
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.app.entity.AnswerSheet;
import com.app.repository.AnswerSheetRepository;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class AnswerSheetServiceImpl implements AnswerSheetService {

    @Autowired
    private AnswerSheetRepository repository;

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

        answerSheets.add(answerSheet);
    }

    workbook.close();
    return answerSheets;
}
}