package com.example.ms1.controller;

import com.example.ms1.KafkaProducerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/kafka")
public class KafkaController {

    private final KafkaProducerService kafkaProducerService;

    @GetMapping("/health")
    public String health() {
        String res = kafkaProducerService.sendAndReceive("health", "ok");
        System.out.println(res);
        return res;
    }
}
