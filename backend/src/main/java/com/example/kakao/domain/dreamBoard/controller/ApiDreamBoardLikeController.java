package com.example.kakao.domain.dreamboard.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.kakao.domain.dreamboard.service.DreamBoardLikeService;
import com.example.kakao.global.dto.response.RsData;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/dreamBoards")
@RequiredArgsConstructor
public class ApiDreamBoardLikeController {

    // @Autowired
    private final DreamBoardLikeService dreamBoardLikeService;


    @Operation(summary = "좋아요확인")
    @GetMapping("/{boardId}/like")
    public ResponseEntity< RsData<Boolean> > hasLiked(@RequestHeader(name = "Authorization") String authorization, @PathVariable("boardId") Long boardId){
        boolean result = false;
        String accessToken = authorization.split(" ")[1];
        try {
            result = dreamBoardLikeService.hasLiked(accessToken, boardId);

        } catch (ResponseStatusException e){
            return new ResponseEntity<>(RsData.of(e.getMessage()), e.getStatusCode());
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(RsData.of(result), HttpStatus.OK);
    }


    @Operation(summary = "좋아요 저장")
    @PostMapping("/{boardId}/like")
    public ResponseEntity< RsData<Boolean> > insertLike(@RequestHeader(name = "Authorization") String authorization, @PathVariable("boardId") Long boardId) {
        boolean result = false;
        String accessToken = authorization.split(" ")[1]; // accessToken 추출
        try {
            result = dreamBoardLikeService.insertLike(accessToken, boardId);

        } catch (ResponseStatusException e){
            return new ResponseEntity<>(RsData.of(e.getMessage()), e.getStatusCode());
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(RsData.of(result), HttpStatus.OK);
    }


    @Operation(summary = "좋아요 취소")
    @DeleteMapping("/{boardId}/like")
    public ResponseEntity< RsData<Boolean> > deleteLike(@RequestHeader(name = "Authorization") String authorization, @PathVariable("boardId") Long boardId) {
        boolean result = false;
        String accessToken = authorization.split(" ")[1]; // accessToken 추출if(result){
        try {
            result = dreamBoardLikeService.deleteLike(accessToken, boardId);

        } catch (ResponseStatusException e){
            return new ResponseEntity<>(RsData.of(e.getMessage()), e.getStatusCode());
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(RsData.of(result), HttpStatus.OK);
    }

}
