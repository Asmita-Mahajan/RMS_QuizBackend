package com.app.kafka;



import com.app.entity.CandidateResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class CandidateResultCompletedProducer {

    private static final String TOPIC = "candidate-results-completed";

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    public void sendMessage(Object candidateResult) {
        kafkaTemplate.send(TOPIC, candidateResult);
    }
}