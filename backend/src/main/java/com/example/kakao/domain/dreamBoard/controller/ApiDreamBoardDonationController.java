package com.example.kakao.domain.dreamboard.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api/v1/dreamBoard")
public class ApiDreamBoardDonationController {
    
    @Operation(summary = "후원 다건 조회", description = "게시글 id 에 해당하는 후원 다건 조회")
    @GetMapping("/{boardId}/donations")
    public void getComments(@PathVariable("boardId") Long boardId) {
        
    }

    @Operation(summary = "후원 단건 조회")
    @GetMapping("/{boardId}/donations/{donationId}")
    public void getCommentById(
        @PathVariable("boardId") Long boardId,
        @PathVariable("donationId") Long donationId
    ) {
        
    }

    @Operation(summary = "후원 저장")
    @PostMapping("/{boardId}/donations")
    public void insertComment(
        @RequestHeader("accessToken") String accessToken,
        @PathVariable("boardId") Long boardId
        // request
    ){

    }

    @Operation(summary = "후원 수정")
    @PutMapping("/{boardId}/donations/{donationId}")
    public void updateComment(
        @RequestHeader("accessToken") String accessToken,
        @PathVariable("boardId") Long boardId,
        @PathVariable("donationId") Long donationId
        // request
    ){

    }

    @Operation(summary = "후원 삭제")
    @DeleteMapping("/{boardId}/donations/{donationId}")
    public void deleteComment(
        @RequestHeader("accessToken") String accessToken,
        @PathVariable("boardId") Long boardId,
        @PathVariable("donationId") Long donationId
    ){

    }
    
}
