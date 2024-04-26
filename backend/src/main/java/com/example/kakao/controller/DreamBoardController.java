package com.example.kakao.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.example.kakao.DTO.DreamBoardDTO;
import com.example.kakao.DTO.ScrollDTO;
import com.example.kakao.service.DreamBoardFileServie;
import com.example.kakao.service.DreamBoardService;

@RestController
@RequestMapping("/api")
public class DreamBoardController {

    @Autowired
    private DreamBoardService boardService;
    @Autowired
    private DreamBoardFileServie boardFileService;

    @GetMapping("/dreamBoards")
    public ResponseEntity<List<DreamBoardDTO>> getBoardList(@ModelAttribute ScrollDTO sc){
        List<DreamBoardDTO> list = boardService.getPagingList(null, 0);
        return ResponseEntity.ok().body(list);
    }

    @GetMapping("/dreamBoards/{idx}")
    public ResponseEntity<DreamBoardDTO> getDreamBoardByIdx(@PathVariable(name = "idx") Long idx) {
        var dto = new DreamBoardDTO();
        return ResponseEntity.ok().body(dto);
    }
    
    @PostMapping(value = "/dreamBoards", consumes = "multipart/form-data; charset=UTF-8")
    @Transactional
    public ResponseEntity<Boolean> insertDreamBoard(MultipartHttpServletRequest request, @ModelAttribute DreamBoardDTO dto) {
        String ipAddress = request.getRemoteAddr();
        String uploadPath = request.getServletContext().getRealPath("/upload/");
        File file2 = new File(uploadPath);
        if(!file2.exists()){
            file2.mkdirs();
        }
        List<MultipartFile> fileList = request.getFiles("file");
        boolean isFirstFile = true;
        
        if(fileList != null && fileList.size() > 0){ // 사진이 있는 경우
            try {
                for(var file : fileList) { // 사진을 조회하며 저장
                    if(file != null && file.getSize() > 0){
                        String saveFileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
                        File saveFile = new File(uploadPath, saveFileName);
                        FileCopyUtils.copy(file.getBytes(), saveFile);
                        
                        if(isFirstFile){

                            isFirstFile = false;
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else { // 사진이 없는 경우

        }

        return ResponseEntity.ok().body(true);
    }

    @PutMapping("/dreamBoards/{idx}")
    public ResponseEntity<Boolean> updateDreamBoard(@PathVariable(name = "idx") Long idx, @RequestBody DreamBoardDTO dto){
        return ResponseEntity.ok().body(true);
    }

    @DeleteMapping("/dreamBoards/{idx}")
    public ResponseEntity<Boolean> deleteDreamBoardByIdx(@PathVariable(name = "idx") Long idx){
        return ResponseEntity.ok().body(true);
    }
}
