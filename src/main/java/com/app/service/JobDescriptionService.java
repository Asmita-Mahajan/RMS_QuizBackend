package com.app.service;




import com.app.entity.JobDescription;
import com.app.repository.JobDescriptionRepository;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JobDescriptionService {

    @Autowired
    private JobDescriptionRepository jobDescriptionRepository;

//    public JobDescription saveJobDescription(MultipartFile file) throws IOException {
//        String filename = file.getOriginalFilename();
//
//        // Check if the job description already exists
//        JobDescription existingJobDescription = jobDescriptionRepository.findByFilename(filename);
//        if (existingJobDescription != null) {
//            throw new IllegalArgumentException("Job description already exists with filename: " + filename);
//        }
//
//        String jdcontent = extractTextFromPDF(file);
//
//        JobDescription jobDescription = new JobDescription();
//        jobDescription.setFilename(filename);
//        jobDescription.setJdcontent(jdcontent);
//
//        return jobDescriptionRepository.save(jobDescription);
//    }

//    public String extractTextFromPDF(MultipartFile file) throws IOException {
//        try (PDDocument document = PDDocument.load(file.getInputStream())) {
//            PDFTextStripper pdfStripper = new PDFTextStripper();
//            return pdfStripper.getText(document);
//        }
//    }
    public List<String> getAllJobDescriptionFilenames() {
        List<JobDescription> jobDescriptions = jobDescriptionRepository.findAll();
        return jobDescriptions.stream()
                .map(JobDescription::getFilename) // Extract filenames
                .collect(Collectors.toList());
    }
}