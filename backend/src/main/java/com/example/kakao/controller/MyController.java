package com.example.kakao.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class MyController {

    @GetMapping("/my")
    @ResponseBody
    public String myAPI() {
        log.info("정보요청");
        return "myasfasfasf";
    }
}
