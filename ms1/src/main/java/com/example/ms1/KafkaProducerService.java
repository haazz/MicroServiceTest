package com.example.ms1;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaProducerService {

    private final ReplyingKafkaTemplate<String, String, String> replyingKafkaTemplate;
    private final String replyTopic = "health-reply"; // 응답 받을 토픽

    public String sendAndReceive(String requestTopic, String message) {
        try {
            // ProducerRecord에 reply 토픽 명시
            ProducerRecord<String, String> record = new ProducerRecord<>(requestTopic, message);
            record.headers().add(KafkaHeaders.REPLY_TOPIC, replyTopic.getBytes());

            // 요청-응답 전송
            RequestReplyFuture<String, String, String> future = replyingKafkaTemplate.sendAndReceive(record);

            // 응답 받기
            ConsumerRecord<String, String> response = future.get(); // 블로킹

            log.info("응답 수신 성공: {}", response.value());
            return response.value();
        } catch (Exception e) {
            log.error("Kafka 요청-응답 실패", e);
            return null;
        }
    }
}

