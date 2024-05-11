package com.example.kakao.domain.user.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.kakao.domain.user.entity.UserEntity;
import com.example.kakao.domain.user.service.UserService;
import com.example.kakao.global.dto.response.RsData;
import com.example.kakao.global.security.jwt.util.JWTUtil;

@RestController
@RequestMapping("/api/v1/users")
public class ApiUserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JWTUtil jwtUtil;

    @GetMapping("")
    public RsData< List<UserEntity> > getUserList(){
        return RsData.of(null, null);
    }

    // 회원가입 주소
    @PostMapping("")
    public RsData< UserEntity > singUp(){
        return null;
    }

}
