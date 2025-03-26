package com.example.ms2.listener;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class KafkaReplyListener {
    private static final String REQUEST_TOPIC = "request-topic";

    @KafkaListener(topics = REQUEST_TOPIC, containerFactory = "kafkaListenerContainerFactory")
    @SendTo  // 기본 Reply 토픽으로 응답
    public Message<String> processRequest(
            @Payload String request,
            @Header(KafkaHeaders.REPLY_TOPIC) byte[] replyTo,
            @Header(KafkaHeaders.CORRELATION_ID) byte[] correlationId
    ) throws InterruptedException {
        // 요청 처리 로직
        String response = "처리 결과(response!): " + request;
        System.out.println(response);
        Thread.sleep((int) (Math.random() * 10) * 1000);
        return MessageBuilder
                .withPayload(response)
                .setHeader(KafkaHeaders.REPLY_TOPIC, replyTo)
                .setHeader(KafkaHeaders.CORRELATION_ID, correlationId)
                .build();
    }

    @KafkaListener(topics = "test-topic", groupId = "test_group", containerFactory = "kafkaListenerContainerFactory")
    public void consume(ConsumerRecord<String, String> record) {
        String message = record.value();
        long offset = record.offset();

        log.info("Consumed message: {}, offset : {}", message, offset);
    }
}