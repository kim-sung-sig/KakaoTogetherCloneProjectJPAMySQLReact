package com.example.kakao.service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.example.kakao.DTO.DreamBoardDTO;

import jakarta.transaction.Transactional;

@Service
public class DreamBoardService {

    // 저장하기
    @Transactional
    public int saveDreamBoard(DreamBoardDTO dto, MultipartHttpServletRequest request){
        int result = 0;
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

        return result;
    }
}
