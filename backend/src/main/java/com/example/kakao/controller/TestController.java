package com.example.kakao.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;

import com.example.kakao.DTO.request.ScrollRequest;

import lombok.extern.slf4j.Slf4j;


@RestController
@Slf4j
public class TestController {

    @GetMapping("/test")
    public String test(@ModelAttribute ScrollRequest request) {
        log.info("정보요청");
        return "Hello, World!" + request;
    }
}
