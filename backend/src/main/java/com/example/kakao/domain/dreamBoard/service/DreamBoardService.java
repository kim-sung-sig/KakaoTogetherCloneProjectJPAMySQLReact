package com.example.kakao.domain.dreamBoard.service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.example.kakao.domain.dreamBoard.entity.DreamBoardEntity;
import com.example.kakao.domain.dreamBoard.repository.DreamBoardRepository;

import jakarta.transaction.Transactional;

@Service
public class DreamBoardService {

    @Autowired
    private DreamBoardRepository dreamBoardRepository;

    // 리스트 얻기
    public List<DreamBoardEntity> getList(){
        List<DreamBoardEntity> list = dreamBoardRepository.findAll();
        return list;
    }
    // 1개 얻기
    public Optional<DreamBoardEntity> getDreamBoardById(Long id) {
        Optional<DreamBoardEntity> optional = dreamBoardRepository.findById(id);
        return optional;
    }

    // 저장하기
    @Transactional
    public boolean saveDreamBoard(DreamBoardEntity entity, MultipartHttpServletRequest request){
        boolean result = false;
        // String ipAddress = request.getRemoteAddr();
        // DreamBoardEntity boardEntity = DreamBoardEntity.builder().category(new DreamBoarcCategoryEntity(null, null))
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
                result = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else { // 사진이 없는 경우

        }

        return result;
    }

}
