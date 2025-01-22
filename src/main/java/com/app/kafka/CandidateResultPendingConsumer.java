

package com.app.kafka;

import com.app.entity.CandidateResult;
import com.app.repository.CandidateResultRepository;
import com.app.service.ResultService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.java.Log;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Log
@Service
public class CandidateResultPendingConsumer {

    @Autowired
    private ResultService resultService;

    @Autowired
    private CandidateResultRepository candidateResultRepository;

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    private static final String RETRY_TOPIC = "candidate-results-pending-retry";
    private static final String DLQ_TOPIC = "candidate-results-pending-dlq";


    @RetryableTopic(attempts = "1") // n-1 retry
    @KafkaListener(topics = "candidate-results-pending", groupId = "candidate-result-group")
    public void listen(String message) {
        try {
            // Convert the message to a CandidateResult object
            CandidateResult candidateResult = new CandidateResult(message);

            // Save the message to MongoDB
            candidateResultRepository.save(candidateResult);
            System.out.println("Received message: " + message);

        } catch (Exception e) {
            // Log the error
           // log.error("Error processing message: {}", e.getMessage(), e);

            // Send to retry topic if an exception occurs
            kafkaTemplate.send(RETRY_TOPIC, message);
        }
    }





    //  @KafkaListener(topics = "candidate-results-pending-dlq", groupId = "candidate-result-group")
    @DltHandler
    public void deadLetterConsume(ConsumerRecord<String, String> record) {
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
