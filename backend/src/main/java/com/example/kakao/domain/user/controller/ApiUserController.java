package com.example.kakao.domain.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.kakao.domain.user.service.UserService;
import com.example.kakao.global.jwt.JWTUtil;

@RestController
@RequestMapping("/api/v1/users")
public class ApiUserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JWTUtil jwtUtil;

    //임시 토큰 발급소
    @GetMapping("/token")
    public String getToken(){
        return jwtUtil.createJwt("accessToken", 1L, "test", "ROLE_USER", 1000 * 60 * 15L);
    }
}
