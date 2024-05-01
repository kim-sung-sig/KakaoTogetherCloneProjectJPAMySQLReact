package com.example.kakao.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.example.kakao.DTO.DreamBoardDTO;
import com.example.kakao.DTO.StatusDTO;
import com.example.kakao.jwt.JWTUtil;
import com.example.kakao.service.DreamBoardService;

@RestController
@RequestMapping("/api/dreamBoards")
public class DreamBoardController {

    @Autowired
    private DreamBoardService boardService;
    @Autowired
    private JWTUtil jwtUtil;

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
