package com.example.kakao.domain.user.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.kakao.domain.dreamboard.service.DreamBoardService;
import com.example.kakao.domain.user.dto.response.UserResponse;
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

    @Autowired
    private DreamBoardService dreamBoardService;

    /**
     * 이거는 관리자페이지 에서 그냥 보내도 될듯? 그래서 거의 사용 안 할 듯?
     * @param authorization
     * @return
     */
    @GetMapping("")
    public ResponseEntity<List<UserResponse>> getUserList(@RequestHeader("Authorization") String authorization) {
        return null;
    }

    /**
     * 유저 객체 하나 얻기
     * @param authorization
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ResponseEntity< UserResponse > getUserById(
        @RequestHeader("Authorization") String authorization,
        @PathVariable("id") Long id
    ) {
        
        return null;
    }

    /**
     * 폼 회원 가입 시 저장
     * @return
     */
    @PostMapping("")
    public RsData< UserEntity > singUp() {
        return null;
    }

    /**
     * 
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(){
        return null;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(){
        return null;
    }
    // 유저 CRUD 끝

    /**
     * 유저가 쓴 게시글 얻기
     */
    @GetMapping("/{id}/dreamBoards")
    public void getDreamBoardsByUser(){

    }

}
