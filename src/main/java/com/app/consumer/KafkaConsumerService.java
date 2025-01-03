//
//package com.app.consumer;
//
//import com.app.entity.CandidateResult;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.apache.kafka.clients.consumer.ConsumerRecord;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.kafka.listener.ConsumerAwareListenerErrorHandler;
//import org.springframework.kafka.support.Acknowledgment;
//import org.springframework.kafka.support.KafkaHeaders;
//import org.springframework.messaging.handler.annotation.Header;
//import org.springframework.retry.annotation.Backoff;
//import org.springframework.retry.annotation.Retryable;
//import org.springframework.stereotype.Service;
//
//@Service
//public class KafkaConsumerService {
//
//    private final ObjectMapper objectMapper = new ObjectMapper();
//    private final KafkaTemplate<String, String> kafkaTemplate;
//
//    @Autowired
//    public KafkaConsumerService(KafkaTemplate<String, String> kafkaTemplate) {
//        this.kafkaTemplate = kafkaTemplate;
//    }
//
//    private static final String RETRY_TOPIC = "candidate_results_retry";
//    private static final String DLQ_TOPIC = "candidate_results_dlq";
//
//    @KafkaListener(topics = "candidate_results", groupId = "group_id", errorHandler = "consumerErrorHandler")
//    @Retryable(
//            value = { Exception.class },
//            maxAttempts = 3,
//            backoff = @Backoff(multiplier = 2, delay = 1000, maxDelay = 10000)
//    )
//    public void consume(ConsumerRecord<String, String> record, Acknowledgment acknowledgment,
//                        @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
//        try {
//            CandidateResult candidateResult = objectMapper.readValue(record.value(), CandidateResult.class);
//            System.out.println("Consumed message: " + candidateResult);
//            acknowledgment.acknowledge();
//        } catch (Exception e) {
//            throw new RuntimeException("Failed to process message", e);
//        }
//    }
//
//    @KafkaListener(topics = RETRY_TOPIC, groupId = "group_id")
//    public void retryConsume(ConsumerRecord<String, String> record, Acknowledgment acknowledgment) {
//        try {
//            CandidateResult candidateResult = objectMapper.readValue(record.value(), CandidateResult.class);
//            System.out.println("Retrying message: " + candidateResult);
//            acknowledgment.acknowledge();
//        } catch (Exception e) {
//            kafkaTemplate.send(DLQ_TOPIC, record.value());
//            acknowledgment.acknowledge();
//        }
//    }
//
//    @KafkaListener(topics = DLQ_TOPIC, groupId = "group_id")
//    public void dlqConsume(ConsumerRecord<String, String> record, Acknowledgment acknowledgment) {
//        System.out.println("Dead-lettered message: " + record.value());
//        acknowledgment.acknowledge();
//    }
//
//    @Bean
//    public ConsumerAwareListenerErrorHandler consumerErrorHandler() {
//        return (message, exception, consumer) -> {
//            kafkaTemplate.send(RETRY_TOPIC, (String) message.getPayload());
//            return null;
//        };
//    }
//}