package com.example.ms1.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaProducerService {
    private final ReplyingKafkaTemplate<String, String, String> replyingKafkaTemplate;
    private static final String REQUEST_TOPIC = "request-topic";
    private final KafkaTemplate<String, String> kafkaTemplate;

    public String sendRequestAndReceiveReply(String request) throws ExecutionException, InterruptedException {
        ProducerRecord<String, String> record =
                new ProducerRecord<>(REQUEST_TOPIC, request);
        record.headers().add(KafkaHeaders.REPLY_TOPIC, "reply-topic".getBytes());

        RequestReplyFuture<String, String, String> replyFuture =
                replyingKafkaTemplate.sendAndReceive(record);

        SendResult<String, String> sendResult = replyFuture.getSendFuture().get();
        System.out.println(sendResult);
        ConsumerRecord<String, String> consumerRecord = replyFuture.get();
        return consumerRecord.value();
    }

    public void sendRequest(String topic, String request) {
        kafkaTemplate.send(topic, request)
                .thenAccept(result -> log.info("전송 성공, {}", result))
                .exceptionally(ex -> {
                    log.error("Kafka 메시지 전송 실패", ex);
                    return null;
                });
    }
}

