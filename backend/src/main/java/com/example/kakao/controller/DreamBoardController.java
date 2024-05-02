package com.example.kakao.controller;

import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.example.kakao.DTO.DreamBoardDTO;
import com.example.kakao.DTO.StatusDTO;
import com.example.kakao.DTO.request.ScrollRequest;
import com.example.kakao.jwt.JWTUtil;
import com.example.kakao.service.DreamBoardService;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api/dreamBoards")
public class DreamBoardController {

    @Autowired
    private DreamBoardService boardService;
    @Autowired
    private JWTUtil jwtUtil;

    @GetMapping(value = "")
    public ResponseEntity<List<DreamBoardDTO>> getBoardList(@ModelAttribute ScrollRequest sc){
        List<DreamBoardDTO> list = new ArrayList<>();//boardService.getPagingList(sc.getLastItemIdx(), sc.getSize(), sc.getCategoryNum(), sc.getSearch());
        
        return ResponseEntity.ok().body(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DreamBoardDTO> getDreamBoardByIdx(@PathVariable(name = "id") Long id) {
        DreamBoardDTO res = new DreamBoardDTO();
        boardService.getDreamBoardByIdx(id);
        return ResponseEntity.ok().body(res);
    }
    
    @PostMapping(value = "", consumes = "multipart/form-data; charset=UTF-8")
    public ResponseEntity<Object> insertDreamBoard(@RequestHeader("access") String accessToken ,@ModelAttribute DreamBoardDTO dto, MultipartHttpServletRequest request) {
        if (accessToken == null || !jwtUtil.validateToken(accessToken)) {
            return ResponseEntity.badRequest().body(new StatusDTO(HttpStatus.BAD_REQUEST.value(), "Invalid accessToken"));
        }
        boolean result = boardService.saveDreamBoard(dto, request);
        if (result) {
            return ResponseEntity.ok().body(new StatusDTO(HttpStatus.OK.value(), "success"));
        } else {
            return ResponseEntity.badRequest().body(new StatusDTO(HttpStatus.BAD_REQUEST.value(), "Failed to save DreamBoard"));
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Boolean> updateDreamBoard(@PathVariable(name = "id") Long id, @RequestBody DreamBoardDTO dto){
        return ResponseEntity.ok().body(true);
    }

    /**
     * 게시글을 삭제하는 주소
     * @param id
     * @return
     */
    @Operation(summary = "게시글 삭제", description = "지정된 id의 게시글을 삭제합니다.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteDreamBoardByIdx(@PathVariable(name = "id") Long id){
        return ResponseEntity.ok().body(true);
    }
}
