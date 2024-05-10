package com.example.kakao.domain.view;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class HomeController {

    @GetMapping("/")
    public String home(HttpServletRequest request){
        String accessToken = request.getHeader("accessToken");
        if(accessToken == null || accessToken.isEmpty()){

        } else {
            System.out.println("로그인 정보 ");
            System.out.println(accessToken);
            System.out.println("=========================");
        }
        return "index";
    }

}
