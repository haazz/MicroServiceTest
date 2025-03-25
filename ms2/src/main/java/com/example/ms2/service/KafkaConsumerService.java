package com.example.ms2.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaConsumerService {

    @KafkaListener(topics = "health", groupId = "health_group", containerFactory = "kafkaListenerContainerFactory")
    @SendTo("health-reply")
    public String consume(ConsumerRecord<String, String> record) throws InterruptedException {
        String message = record.value();
        long offset = record.offset();
//        Thread.sleep(10000);
        log.info("Consumed message: {}, offset : {}", message, offset);
        String reply = "응답 완료 reply: " + message;
        return reply;
    }
}
