package com.example.kakao.domain.dreamboard.service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import com.example.kakao.domain.dreamboard.entity.DreamBoardEntity;
import com.example.kakao.domain.dreamboard.entity.DreamBoardFileEntity;
import com.example.kakao.domain.dreamboard.repository.DreamBoardFileRepository;
import com.example.kakao.domain.dreamboard.repository.DreamBoardRepository;

import jakarta.transaction.Transactional;

@Service
public class DreamBoardService {

    @Autowired
    private DreamBoardRepository dreamBoardRepository;
    @Autowired
    private DreamBoardFileRepository dreamBoardFileRepository;

    // 리스트 얻기
    public List<DreamBoardEntity> getList(){
        List<DreamBoardEntity> list = dreamBoardRepository.findAll();
        return list;
    }
    // 1개 얻기
    public Optional<DreamBoardEntity> findById(Long id) {
        Optional<DreamBoardEntity> optional = dreamBoardRepository.findById(id);
        return optional;
    }

    // 저장하기
    @Transactional
    public boolean saveDreamBoard(DreamBoardEntity entity, List<MultipartFile> files, String uploadPath){
        boolean result = false;
        File file2 = new File(uploadPath);
        if(!file2.exists()){
            file2.mkdirs();
        }
        boolean isFirstFile = true;
        
        if(files != null && files.size() > 0){ // 사진이 있는 경우
            try {
                for(MultipartFile file : files) { // 사진을 조회하며 저장
                    if(file != null && file.getSize() > 0){
                        String saveFileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
                        File saveFile = new File(uploadPath, saveFileName);
                        FileCopyUtils.copy(file.getBytes(), saveFile);
                        
                        if(isFirstFile){
                            entity.setThumbnail(saveFileName); // 썸네일 넣어주고
                            dreamBoardRepository.save(entity); // 저장
                            isFirstFile = false;
                        }
                        DreamBoardFileEntity fileEntity = DreamBoardFileEntity.builder()
                                                            .board(DreamBoardEntity.builder().id(entity.getId()).build())
                                                            .saveFileName(saveFileName)
                                                            .build();
                        dreamBoardFileRepository.save(fileEntity);
                    }
                }
                result = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        } // 사진이 없으면 false
        return result;
    }

    // 수정하기
    @Transactional
    public boolean updateDreamBoard(DreamBoardEntity entity, List<MultipartFile> files, String uploadPath){
        boolean result = false;
        // String uploadPath = request.getServletContext().getRealPath("/upload/");
        /*
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
         */
        return result;
    }


    // 삭제하기
    @Transactional
    public boolean deleteById(Long id){
        boolean result = false;
        try {
            dreamBoardRepository.deleteById(id);
            result = true;
        } catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    // 조회수 증가
    public void incrementReadCount(Long id){
        dreamBoardRepository.incrementReadCount(id);
    }
}
