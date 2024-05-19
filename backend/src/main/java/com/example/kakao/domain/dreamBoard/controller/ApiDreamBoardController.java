package com.example.kakao.domain.dreamboard.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.example.kakao.domain.dreamboard.dto.request.DreamBoardUpdateRequest;
import com.example.kakao.domain.dreamboard.dto.request.DreamBoardUploadRequest;
import com.example.kakao.domain.dreamboard.dto.response.DreamBoardResponse;
import com.example.kakao.domain.dreamboard.entity.DreamBoardEntity;
import com.example.kakao.domain.dreamboard.service.DreamBoardService;
import com.example.kakao.global.dto.request.ScrollRequest;
import com.example.kakao.global.dto.response.RsData;
import com.example.kakao.global.exception.EntityNotFoundException;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/dreamBoards")
public class ApiDreamBoardController {

    @Autowired
    private DreamBoardService boardService;

    @Operation(summary = "게시글 다건 조회", description = "지정된 id에 해당하는 게시글을 조회합니다.")
    @GetMapping(value = "")
    public ResponseEntity< List<DreamBoardResponse> > getBoardList(
        HttpServletRequest request,
        @ModelAttribute ScrollRequest sc
    ){
        log.info("getBoardList 호출 ScrollVO => {}", sc);
        List<DreamBoardResponse> resultList = null;
        try {
            resultList = boardService.getEntitysWithPagination(sc.getLastItemId(), sc.getSize(), sc.getCategoryNum(), sc.getSearch());
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(resultList, HttpStatus.OK);
    }

    @Operation(summary = "게시글 단건 조회", description = "지정된 id에 해당하는 게시글을 조회합니다.")
    @GetMapping("/{id}")
    public ResponseEntity<DreamBoardResponse> getDreamBoardById(@PathVariable(name = "id") Long id) {
        DreamBoardResponse result = null;
        try {
            result = boardService.findById(id);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        } catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);        
    }

    @Operation(summary = "게시글 저장", description = "게시글을 저장합니다.")
    @PostMapping(value = "", consumes = "multipart/form-data; charset=UTF-8")
    @Transactional
    public ResponseEntity<?> insertDreamBoard(
        @RequestHeader(name = "Authorization") String authorization,
        @ModelAttribute DreamBoardUploadRequest uploadRequest,
        HttpServletRequest req
    ) {
        log.info("게시글 저장 실행");
        Boolean result = false;
        String accessToken = authorization.split(" ")[1]; // accessToken 추출
        uploadRequest.setIp(req.getRemoteAddr());
        try {
            result = boardService.saveDreamBoard(accessToken, uploadRequest, req);
        } catch (IOException e) {
            return new ResponseEntity<>("IOException",HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /*
     * Todo 수정은 이미 저장되어있는 사진은 안건드리는 로직을 추가해야한다. 도전!
     */
    @Operation(summary = "게시글 수정", description = "지정된 id의 게시글을 수정합니다.")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateDreamBoard(
        @RequestHeader(name = "Authorization") String authorization,
        @PathVariable(name = "id") Long id,
        @ModelAttribute DreamBoardUpdateRequest updateRequest,
        HttpServletRequest request
    ){
        log.info("게시글 수정 실행");
        Boolean result = false;
        String accessToken = authorization.split(" ")[1]; // accessToken 추출
        updateRequest.setId(id);
        updateRequest.setIp(request.getRemoteAddr());


        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    @Data
    public static class RemoveResponse{
        private final DreamBoardEntity dreamBoardEntity;
    }

    // 게시글을 삭제하는 주소
    @Operation(summary = "게시글 삭제", description = "지정된 id의 게시글을 삭제합니다.")
    @DeleteMapping("/{id}")
    public RsData< DreamBoardResponse > deleteDreamBoardByIdx(
        @RequestHeader(name = "Authorization") String authorization,
        @PathVariable(name = "id") Long id
    ){
        return null;
    }

}
