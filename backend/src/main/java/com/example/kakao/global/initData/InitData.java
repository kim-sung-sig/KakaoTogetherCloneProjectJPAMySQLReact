package com.example.kakao.global.initData;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import com.example.kakao.domain.dreamBoard.service.DreamBoardService;

@Configuration
public class InitData {

    // @Bean
    CommandLineRunner initData(DreamBoardService service) {
        return args -> {
            service.saveDreamBoard(null, null);
        };
    }
}
