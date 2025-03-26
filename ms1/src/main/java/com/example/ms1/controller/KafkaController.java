package com.example.ms1.controller;

import com.example.ms1.service.KafkaProducerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/kafka")
public class KafkaController {

    private final KafkaProducerService kafkaProducerService;

    @GetMapping("/health")
    public String health() throws ExecutionException, InterruptedException {
        String res = kafkaProducerService.sendRequestAndReceiveReply("JelloWorld!");
        System.out.println(res);
        return res;
    }

    @GetMapping("/async")
    public String async() throws ExecutionException, InterruptedException {
        kafkaProducerService.sendRequest("test-topic", "test!");
        return "test";
    }
}
