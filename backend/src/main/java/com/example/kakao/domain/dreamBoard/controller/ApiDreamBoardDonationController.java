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
@RequestMapping("/api/v1/dreamBoard")
public class ApiDreamBoardDonationController {
    
    @Operation(summary = "후원 다건 조회", description = "게시글 id 에 해당하는 후원 다건 조회")
    @GetMapping("/{boardId}/donations")
    public ResponseEntity<RsData< ? >> getDonations(@PathVariable("boardId") Long boardId, @ModelAttribute ScrollRequest sc){
        return null;
    }

    @Operation(summary = "후원 단건 조회")
    @GetMapping("/{boardId}/donations/{donationId}")
    public ResponseEntity<RsData< ? >> getDonationById(@PathVariable("boardId") Long boardId, @PathVariable("donationId") Long donationId){
        return null;
    }

    @Operation(summary = "후원 저장")
    @PostMapping("/{boardId}/donations")
    public ResponseEntity<RsData< ? >> insertComment(
        @RequestHeader("accessToken") String accessToken,
        @PathVariable("boardId") Long boardId
        // request
    ){
        return null;
    }

    @Operation(summary = "후원 수정")// 후원금액 증가 혹은 응원글 변경
    @PutMapping("/{boardId}/donations/{donationId}")
    public ResponseEntity<RsData< ? >> updateComment(
        @RequestHeader("accessToken") String accessToken,
        @PathVariable("boardId") Long boardId,
        @PathVariable("donationId") Long donationId
        // request
    ){
        return null;
    }

    @Operation(summary = "후원 삭제")// 후원 숨기기
    @DeleteMapping("/{boardId}/donations/{donationId}")
    public ResponseEntity<RsData< ? >> deleteComment(
        @RequestHeader("accessToken") String accessToken,
        @PathVariable("boardId") Long boardId,
        @PathVariable("donationId") Long donationId
    ){
        return null;
    }
    
}
