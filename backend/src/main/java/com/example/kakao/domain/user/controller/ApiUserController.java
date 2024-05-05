package com.example.kakao.domain.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.kakao.domain.user.service.UserService;

@RestController
@RequestMapping("/api/v1/users")
public class ApiUserController {

    @Autowired
    private UserService userService;

    
}
