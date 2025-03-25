package com.example.ms2.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ms2")
public class MainController {
    @GetMapping("/1")
    public String mainControl() {
        return "ms2";
    }
}
