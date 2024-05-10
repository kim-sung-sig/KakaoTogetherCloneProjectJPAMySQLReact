package com.example.kakao.domain.dreamboard.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.kakao.domain.dreamboard.service.DreamBoardLikeService;
import com.example.kakao.global.dto.response.RsData;

import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/dreamBoards")
public class ApiDreamBoardLikeController {

    @Autowired
    private DreamBoardLikeService dreamBoardLikeService;

    @Operation(summary = "좋아요확인")
    @GetMapping("/{boardId}/like")
    public RsData< Boolean > hasLiked(
        //@RequestHeader(name = "accessToken") String accessToken,
        @PathVariable("boardId") Long boardId
    ){
        boolean result = dreamBoardLikeService.hasLiked(boardId, 1L);
        if(result){
            return RsData.of("S-1", "%d번 유저 %d번 게시글에 좋아요함".formatted(1L, boardId), result);
        } else {
            return RsData.of("F-1", "%d번 유저 %d번 게시글에 좋아요 안함".formatted(1L, boardId), result);
        }
    }

    @Operation(summary = "좋아요 저장")
    @PostMapping("/{boardId}/like")
    public RsData< Boolean > insertLike(
        // @RequestHeader(name = "accessToken") String accessToken,
        @PathVariable("boardId") Long boardId
    ) {
        boolean result = dreamBoardLikeService.insertLike(boardId, 1L);
        if(result){
            return RsData.of("S-2", "좋아요 성공", result);
        } else {
            return RsData.of("F-2", "좋아요 실패", result);
        }
    }

    @Operation(summary = "좋아요 취소")
    @DeleteMapping("/{boardId}/like")
    public RsData< Boolean > deleteLike(
        // @RequestHeader(name = "accessToken") String accessToken,
        @PathVariable("boardId") Long boardId
    ) {
        log.info("{}", boardId);
        boolean result = dreamBoardLikeService.deleteLike(boardId, 1L);
        log.info("{}", result);
        if(result){
            return RsData.of("S-4", "좋아요 삭제 성공", result);
        } else {
            return RsData.of("F-4", "좋아요 삭제 실패", result);
        }
    }

}
