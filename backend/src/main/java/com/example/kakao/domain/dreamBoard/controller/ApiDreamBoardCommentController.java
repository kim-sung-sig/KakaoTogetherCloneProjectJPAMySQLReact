package com.example.kakao.domain.dreamboard.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.kakao.global.dto.request.ScrollRequest;
import com.example.kakao.global.dto.response.RsData;

import io.swagger.v3.oas.annotations.Operation;


@RestController
@RequestMapping("/api/v1/dreamBoards")
public class ApiDreamBoardCommentController {

    @Operation(summary = "댓글 다건 조회")
    @GetMapping("/{boardId}/comments")
    public ResponseEntity<RsData< ? >> getComments(@PathVariable("boardId") Long boardId, @ModelAttribute ScrollRequest sc){
        return null;
    }

    @Operation(summary = "댓글 단건 조회")
    @GetMapping("/{boardId}/comments/{commentId}")
    public ResponseEntity<RsData< ? >> getCommentById(@PathVariable("boardId") Long boardId, @PathVariable("commentId") Long commentId){
        return null;
    }

    @Operation(summary = "댓글 저장")
    @PostMapping("/{boardId}/comments")
    public ResponseEntity<RsData< ? >> insertComment(
        @RequestHeader("accessToken") String accessToken,
        @PathVariable("boardId") Long boardId
        // request
    ){
        return null;
    }

    @Operation(summary = "댓글 수정")
    @PutMapping("/{boardId}/comments/{commentId}")
    public ResponseEntity<RsData< ? >> updateComment(
        @RequestHeader("accessToken") String accessToken,
        @PathVariable("boardId") Long boardId,
        @PathVariable("commentId") Long commentId
        // request
    ){
        return null;
    }

    @Operation(summary = "댓글 삭제")
    @DeleteMapping("/{boardId}/comments/{commentId}")
    public ResponseEntity<RsData< ? >> deleteComment(
        @RequestHeader("accessToken") String accessToken,
        @PathVariable("boardId") Long boardId,
        @PathVariable("commentId") Long commentId
    ){
        return null;
    }
    
}
