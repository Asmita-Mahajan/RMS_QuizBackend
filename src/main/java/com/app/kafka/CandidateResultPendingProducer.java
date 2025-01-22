//package com.app.kafka;
//
//import com.app.entity.CandidateResult;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.stereotype.Service;
//import java.util.List;
//@Service
//public class CandidateResultPendingProducer {
//
//    private static final String TOPIC = "candidate-results-pending";
//
//    @Autowired
//    private KafkaTemplate<String, Object> kafkaTemplate;
//
//    public void sendMessage(Object candidateResult) {
//        kafkaTemplate.send(TOPIC, candidateResult);
//    }
//
//}
//

package com.app.kafka;

import com.app.entity.CandidateResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CandidateResultPendingProducer {

    private static final String TOPIC = "candidate-results-pending";


    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

//    public void sendMessages(List<CandidateResult> candidateResults) {
//        candiatelistkafkaTemplate.send(TOPIC, candidateResults);
//    }

    public void sendMessages(List<CandidateResult> candidateResults) {
        for (CandidateResult candidateResult : candidateResults) {
            kafkaTemplate.send(TOPIC, candidateResult); // Send each entity individually
        }
    }
}