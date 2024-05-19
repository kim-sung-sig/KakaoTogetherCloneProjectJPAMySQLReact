package com.example.kakao.domain.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class AdminController {

    @GetMapping("/")
    public String index() {
        return "index";
    }
    
    @GetMapping("/admin")
    public String index2(){
        return "index";
    }
    
}
