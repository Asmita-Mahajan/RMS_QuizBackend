



package com.app.kafka;

import com.app.entity.CandidateResult;
import com.app.service.ResultService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.java.Log;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Log
@Service
public class CandidateResultCompletedConsumer {

    @Autowired
    private ResultService resultService;

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    private static final String RETRY_TOPIC = "candidate-results-completed-retry";
    private static final String DLQ_TOPIC = "candidate-results-completed-dlq";



    @KafkaListener(topics = "candidate-results-completed", groupId = "candidate-result-group")
    public void consume(ConsumerRecord<String, CandidateResult> record) {
        try {
            CandidateResult candidateResult = record.value();

            // Convert candidate result to JSON
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonResult = objectMapper.writeValueAsString(candidateResult);

            // Print JSON result to console
            System.out.println(jsonResult);
            log.info(jsonResult);

        } catch (Exception e) {
            // Send to retry topic if an exception occurs
            kafkaTemplate.send(RETRY_TOPIC, record.value());
        }
    }





    //  @KafkaListener(topics = "candidate-results-completed-dlq", groupId = "candidate-result-group")
    @DltHandler
    public void deadLetterConsumeCompleted(ConsumerRecord<String, String> record) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            CandidateResult candidateResult = objectMapper.readValue(record.value(), CandidateResult.class);
            // Log Dead Letter Queue messages for analysis

            System.err.println("Dead Letter Queue: " + candidateResult);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
