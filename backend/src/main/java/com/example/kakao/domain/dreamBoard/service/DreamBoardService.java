package com.example.kakao.domain.dreamboard.service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import com.example.kakao.domain.dreamboard.dto.request.DreamBoardUploadRequest;
import com.example.kakao.domain.dreamboard.dto.response.DreamBoardResponse;
import com.example.kakao.domain.dreamboard.entity.DreamBoardCategoryEntity;
import com.example.kakao.domain.dreamboard.entity.DreamBoardEntity;
import com.example.kakao.domain.dreamboard.entity.DreamBoardFileEntity;
import com.example.kakao.domain.dreamboard.repository.DreamBoardCategoryRepository;
import com.example.kakao.domain.dreamboard.repository.DreamBoardFileRepository;
import com.example.kakao.domain.dreamboard.repository.DreamBoardRepository;
import com.example.kakao.domain.user.entity.UserEntity;
import com.example.kakao.domain.user.repository.UserRepository;
import com.example.kakao.global.exception.EntityNotFoundException;
import com.example.kakao.global.security.jwt.util.JWTUtil;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DreamBoardService {

    // @Autowired
    private final String fileDirPath;
    private final JWTUtil jwtUtil;
    private final UserRepository userRepository;
    private final DreamBoardRepository dreamBoardRepository;
    private final DreamBoardCategoryRepository dreamBoardCategoryRepository;
    private final DreamBoardFileRepository dreamBoardFileRepository;

    public DreamBoardService(@Value("${custom.fileDirPath}") String fileDirPath,
                            JWTUtil jwtUtil,
                            UserRepository userRepository,
                            DreamBoardRepository dreamBoardRepository,
                            DreamBoardCategoryRepository dreamBoardCategoryRepository,
                            DreamBoardFileRepository dreamBoardFileRepository
    ){
        this.fileDirPath = fileDirPath;
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
        this.dreamBoardRepository = dreamBoardRepository;
        this.dreamBoardCategoryRepository = dreamBoardCategoryRepository;
        this.dreamBoardFileRepository = dreamBoardFileRepository;
    }

    // 리스트 얻기
    public List<DreamBoardResponse> getEntitysWithPagination(Long lastId, int size, Long categoryId, String search) throws Exception{
        log.info("변수 에러 검사 시작");
        if((lastId != null && lastId < 0) || size < 0){
            throw new IllegalArgumentException("유효하지않은 lastId or size");
        }
        if(categoryId != null){
            Optional<DreamBoardCategoryEntity> categoryEntity = dreamBoardCategoryRepository.findById(categoryId);
            if(!categoryEntity.isPresent()){
                throw new IllegalArgumentException("유효하지않은 categoryId");
            }
        }
        log.info("변수 에러 검사 종료");

        Pageable pageable = PageRequest.of(0, size);
        Page<DreamBoardEntity> page = dreamBoardRepository.findAllByCondition(lastId, categoryId, search, pageable);

        return page.getContent().stream()
                .map(entity -> new DreamBoardResponse(entity))
                .collect(Collectors.toList());
    }
    // 1개 얻기
    public DreamBoardResponse findById(Long id) throws EntityNotFoundException {
        Optional<DreamBoardEntity> optional = dreamBoardRepository.findById(id);
        if(optional.isPresent()){
            return new DreamBoardResponse(optional.get());
        } else {
            throw new EntityNotFoundException("id에 해당하는 게시글이 존재하지 않습니다.");
        }
    }

    // 저장하기
    @Transactional
    public Boolean saveDreamBoard(String accessToken, DreamBoardUploadRequest uploadRequest
    ) throws IOException, EntityNotFoundException{
        // 검증 시작
        Long userId = jwtUtil.getId(accessToken);
        Optional<UserEntity> userEntity = userRepository.findById(userId);
        if(!userEntity.isPresent()){
            throw new EntityNotFoundException("탈퇴한 회원 Or 이상한 회원");
        }

        Optional<DreamBoardCategoryEntity> categoryEntity = dreamBoardCategoryRepository.findById(uploadRequest.getCategoryFk());
        if(!categoryEntity.isPresent()){
            throw new EntityNotFoundException("카테고리번호 이상");
        }
        // 일단 검증 완료
        //TODO 널 검증 해야댐
        
        // requestToEntity
        DreamBoardEntity boardEntity = DreamBoardEntity.builder()
                .user(UserEntity.builder().id(userId).build())
                .category(DreamBoardCategoryEntity.builder().id(uploadRequest.getCategoryFk()).build())
                .title(uploadRequest.getTitle()).content(uploadRequest.getContent())
                .tag1(uploadRequest.getTag1()).tag2(uploadRequest.getTag1()).tag3(uploadRequest.getTag1())
                .targetPrice(uploadRequest.getTargetPrice())
                .startDate(uploadRequest.getStartDate()).endDate(uploadRequest.getEndDate())
                .ip(uploadRequest.getIp()).build();
        // file 객체
        List<MultipartFile> files = uploadRequest.getFile();

        // 저장 실행!
        File uploadDir = new File(fileDirPath);
        if(!uploadDir.exists()){
            uploadDir.mkdirs();
        }

        boolean isFirstFile = true;
        if(files != null && files.size() > 0){ // 사진이 있는 경우
            for(MultipartFile file : files) { // 사진을 조회하며 저장
                if(file != null && file.getSize() > 0){
                    String saveFileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
                    File saveFile = new File(uploadDir, saveFileName);
                    FileCopyUtils.copy(file.getBytes(), saveFile);
                    
                    if(isFirstFile){
                        boardEntity.setThumbnail(saveFileName); // 썸네일 넣어주고
                        dreamBoardRepository.save(boardEntity); // 저장
                        // System.out.println(entity); // id 자동으로 넣어줫냐?
                        isFirstFile = false;
                    }
                    DreamBoardFileEntity fileEntity = DreamBoardFileEntity.builder()
                            .board(boardEntity)
                            .saveFileName(saveFileName).build();
                    dreamBoardFileRepository.save(fileEntity);
                }
            }
        } else { // 사진이 없으면!
            //TODO 일단 이런 익셉션으로 던져보자..
            throw new EntityNotFoundException("사진없어서");
        }
        return true;
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
