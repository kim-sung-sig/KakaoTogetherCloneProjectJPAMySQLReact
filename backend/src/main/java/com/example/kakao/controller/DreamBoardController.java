package com.example.kakao.controller;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.FileCopyUtils;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.example.kakao.DTO.DreamBoardDTO;
import com.example.kakao.DTO.request.ScrollRequest;
import com.example.kakao.jwt.JWTService;
import com.example.kakao.jwt.JWTUtil;
import com.example.kakao.service.DreamBoardService;

import jakarta.transaction.Transactional;

@RestController
@RequestMapping("/api/dreamBoards")
public class DreamBoardController {

    @Autowired
    private DreamBoardService boardService;
    @Autowired
    private JWTService jwtService;

    /*
    @GetMapping(value = {"","/"})
    public ResponseEntity<List<DreamBoardDTO>> getBoardList(@ModelAttribute ScrollRequest sc){
        List<DreamBoardDTO> list = boardService.getPagingList(sc.getLastItemIdx(), sc.getSize(), sc.getCategoryNum(), sc.getSearch());
        
        return ResponseEntity.ok().body(list);
    }
    */

    @GetMapping("/{idx}")
    public ResponseEntity<DreamBoardDTO> getDreamBoardByIdx(@PathVariable(name = "idx") Long idx) {
        DreamBoardDTO res = new DreamBoardDTO();
        return ResponseEntity.ok().body(res);
    }
    
    @PostMapping(value = {"","/"}, consumes = "multipart/form-data; charset=UTF-8")
    public ResponseEntity<Boolean> insertDreamBoard(@RequestHeader("access") String accessToken ,@ModelAttribute DreamBoardDTO dto, MultipartHttpServletRequest request) {
        if(jwtService.validateToken(accessToken)){
            
        }
        return ResponseEntity.ok().body(true);
    }
    /*
    @PutMapping("/{idx}")
    public ResponseEntity<Boolean> updateDreamBoard(@PathVariable(name = "idx") Long idx, @RequestBody DreamBoardDTO dto){
        return ResponseEntity.ok().body(true);
    }

    @DeleteMapping("/{idx}")
    public ResponseEntity<Boolean> deleteDreamBoardByIdx(@PathVariable(name = "idx") Long idx){
        return ResponseEntity.ok().body(true);
    }
     */
}
