package com.example.kakao.domain.dreamboard.service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import com.example.kakao.domain.dreamboard.dto.response.DreamBoardResponse;
import com.example.kakao.domain.dreamboard.entity.DreamBoardEntity;
import com.example.kakao.domain.dreamboard.entity.DreamBoardFileEntity;
import com.example.kakao.domain.dreamboard.repository.DreamBoardFileRepository;
import com.example.kakao.domain.dreamboard.repository.DreamBoardRepository;
import com.example.kakao.global.dto.response.RsData;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

@Service
// @RequiredArgsConstructor
public class DreamBoardService {

    @Autowired
    private EntityManager entityManager;
    @Autowired
    private DreamBoardRepository dreamBoardRepository;
    @Autowired
    private DreamBoardFileRepository dreamBoardFileRepository;

    // 리스트 얻기
    public List<DreamBoardEntity> getEntitysWithPagination(Long lastItemId, int size, Long categoryId, String search){
        Pageable pageable = PageRequest.of(0, size);
        Page<DreamBoardEntity> page = dreamBoardRepository.search(lastItemId, categoryId, search, pageable);
        return page.getContent();
    }
    // 1개 얻기
    public Optional<DreamBoardEntity> findById(Long id) {
        Optional<DreamBoardEntity> optional = dreamBoardRepository.findById(id);
        return optional;
    }

    // 저장하기
    @Transactional
    public RsData< DreamBoardResponse > saveDreamBoard(DreamBoardEntity entity, List<MultipartFile> files, String uploadPath){
        try {
            File file2 = new File(uploadPath);
            if(!file2.exists()){
                file2.mkdirs();
            }

            boolean isFirstFile = true;
            if(files != null && files.size() > 0){ // 사진이 있는 경우
                for(MultipartFile file : files) { // 사진을 조회하며 저장
                    if(file != null && file.getSize() > 0){
                        String saveFileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
                        System.out.println(saveFileName);
                        File saveFile = new File(uploadPath, saveFileName);
                        FileCopyUtils.copy(file.getBytes(), saveFile);
                        
                        if(isFirstFile){
                            entity.setThumbnail(saveFileName); // 썸네일 넣어주고
                            dreamBoardRepository.save(entity); // 저장
                            // System.out.println(entity); // id 자동으로 넣어줫냐?
                            isFirstFile = false;
                        }
                        DreamBoardFileEntity fileEntity = DreamBoardFileEntity.builder()
                                                            .board(DreamBoardEntity.builder().id(entity.getId()).build())
                                                            .saveFileName(saveFileName)
                                                            .build();
                        dreamBoardFileRepository.save(fileEntity);
                    }
                }
            } else { // 사진이 없으면!
                return RsData.of("F-", "사진없어서 반례");
            }
        } catch (IOException e) {
            e.printStackTrace();
            return RsData.of("F-", "사진 저장중 에러발생");
        }
        entityManager.clear(); // 영속성 컨텍스트 지우기
        return dreamBoardRepository.findById(entity.getId()).map(en -> 
            RsData.of("S-2", "게시글 저장 성공", new DreamBoardResponse(en))
        ).orElseGet(() ->
            RsData.of("F-2", "게시글 저장 실패")
        );
    }

    // 수정하기
    @Transactional
    public boolean updateDreamBoard(DreamBoardEntity entity, List<MultipartFile> files, String uploadPath){
        boolean result = false;
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
