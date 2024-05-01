package com.example.kakao;

import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.kakao.repository.DreamBoardCategoryRepository;
import com.example.kakao.repository.DreamBoardRepository;
import com.example.kakao.repository.UserRepository;

@SpringBootTest
public class Test {
    @Autowired
    private DreamBoardCategoryRepository boardCategoryRepository;

    @Autowired
    private DreamBoardRepository boardRepository;

    @Autowired
    private UserRepository userRepository;

}
