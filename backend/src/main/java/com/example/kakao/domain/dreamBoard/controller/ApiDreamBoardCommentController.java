package com.example.kakao.domain.dreamboard.controller;

import org.springframework.http.HttpStatus;
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
import org.springframework.web.server.ResponseStatusException;

import com.example.kakao.domain.dreamboard.dto.response.DreamBoardCommentResponse;
import com.example.kakao.domain.dreamboard.service.DreamBoardCommentService;
import com.example.kakao.global.dto.request.ScrollRequest;
import com.example.kakao.global.dto.response.PagingResponse;
import com.example.kakao.global.dto.response.RsData;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/api/v1/dreamBoards")
@RequiredArgsConstructor
public class ApiDreamBoardCommentController {

    //@Autowired
    private final DreamBoardCommentService commentService;


    @Operation(summary = "댓글 다건 조회")
    @GetMapping("/{boardId}/comments")
    public ResponseEntity<RsData< PagingResponse<DreamBoardCommentResponse> >> getComments(@PathVariable("boardId") Long boardId, @ModelAttribute ScrollRequest sc){
        PagingResponse<DreamBoardCommentResponse> data = null;
        try {
            data = commentService.findAllByBoardIdAndCondition(boardId, sc.getLastItemId(), sc.getSize());

        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(RsData.of(e.getMessage()), e.getStatusCode());
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(RsData.of("ok", data), HttpStatus.OK);
    }


    @Operation(summary = "댓글 단건 조회")
    @GetMapping("/{boardId}/comments/{commentId}")
    public ResponseEntity<RsData< DreamBoardCommentResponse >> getCommentById(@PathVariable("boardId") Long boardId, @PathVariable("commentId") Long commentId){
        DreamBoardCommentResponse data = null;
        try {
            data = commentService.findByBoardIdAndId(boardId, commentId);
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(RsData.of(e.getMessage()), e.getStatusCode());
        } catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(RsData.of("ok", data), HttpStatus.OK);
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
