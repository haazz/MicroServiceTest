package com.example.ms1.listener;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

@Component
public class KafkaRequestListener {
//    private static final String REQUEST_TOPIC = "request-topic";
//
//    @KafkaListener(topics = REQUEST_TOPIC, containerFactory = "kafkaListenerContainerFactory")
//    @SendTo  // 기본 Reply 토픽으로 응답
//    public String processRequest(
//            @Payload String request,
//            @Header(KafkaHeaders.REPLY_TOPIC) byte[] replyTo
//    ) {
//        // 요청 처리 로직
//        return "처리 완료: " + request;
//    }
}