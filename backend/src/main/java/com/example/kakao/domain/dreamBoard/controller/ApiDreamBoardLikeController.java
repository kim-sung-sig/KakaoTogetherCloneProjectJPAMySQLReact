package com.example.kakao.domain.dreamboard.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;


@RestController
@RequestMapping("/api/v1/dreamBoards")
public class ApiDreamBoardLikeController {

    @Operation(summary = "좋아요확인")
    @GetMapping("/{boardId}/like")
    public void checkLike(@PathVariable("boardId") Long boardId, @RequestHeader(name = "accessToken") String accessToken){
        
    }

    @Operation(summary = "좋아요 저장")
    @PostMapping("/{boardId}/like")
    public void insertLike(@PathVariable("boardId") Long boardId, @RequestHeader(name = "accessToken") String accessToken) {

    }

    @Operation(summary = "좋아요 취소")
    @DeleteMapping("/{boardId}/like")
    public void deleteLike(@PathVariable("boardId") Long boardId, @RequestHeader(name = "accessToken") String accessToken) {

    }

}
