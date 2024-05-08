package com.example.kakao.domain.dreamboard.controller;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.kakao.domain.dreamboard.dto.request.DreamBoardUpdateRequest;
import com.example.kakao.domain.dreamboard.dto.request.DreamBoardWriteRequest;
import com.example.kakao.domain.dreamboard.dto.response.DreamBoardResponse;
import com.example.kakao.domain.dreamboard.entity.DreamBoardCategoryEntity;
import com.example.kakao.domain.dreamboard.entity.DreamBoardEntity;
import com.example.kakao.domain.dreamboard.entity.DreamBoardFileEntity;
import com.example.kakao.domain.dreamboard.repository.DreamBoardCategoryRepository;
import com.example.kakao.domain.dreamboard.repository.DreamBoardFileRepository;
import com.example.kakao.domain.dreamboard.repository.DreamBoardRepository;
import com.example.kakao.domain.dreamboard.service.DreamBoardService;
import com.example.kakao.domain.user.entity.UserEntity;
import com.example.kakao.domain.user.repository.UserRepository;
import com.example.kakao.global.RsData.RsData;
import com.example.kakao.global.dto.request.ScrollRequest;
import com.example.kakao.global.jwt.JWTUtil;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.persistence.EntityManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/dreamBoards")
public class ApiDreamBoardController {

    @Autowired
    private JWTUtil jwtUtil;
    @Autowired
    private DreamBoardService boardService;
    @Autowired
    private DreamBoardRepository dreamBoardRepository;
    @Autowired
    private DreamBoardFileRepository dreamBoardFileRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DreamBoardCategoryRepository dreamBoardCategoryRepository;
    @Autowired
    private EntityManager entityManager;


    @Operation(summary = "게시글 다건 조회", description = "지정된 id에 해당하는 게시글을 조회합니다.")
    @GetMapping(value = "")
    public RsData< List<DreamBoardResponse> > getBoardList(@ModelAttribute ScrollRequest sc){
        if(sc.getSize() == null){
            return RsData.of("F-0", "size를 넣어주세요.");
        }
        List<DreamBoardEntity> list = boardService.getEntitysWithPagination(sc.getLastItemId(), sc.getSize(), sc.getCategoryNum(), sc.getSearch());
        List<DreamBoardResponse> resultList = null;
        if(list != null && list.size() != 0){
            resultList = list.stream()
            .map(entity -> new DreamBoardResponse(entity))
            .collect(Collectors.toList());
        }
        return RsData.of("S-0", "success", resultList);
    }

    @Operation(summary = "게시글 단건 조회", description = "지정된 id에 해당하는 게시글을 조회합니다.")
    @GetMapping("/{id}")
    public RsData< DreamBoardResponse > getDreamBoardById(@PathVariable(name = "id") Long id) {
        return boardService.findById(id).map((entity) -> 
            RsData.of("S-1", "success", new DreamBoardResponse(entity))
        ).orElseGet(() ->
            RsData.of("F-1", "%d번 글은 존재하지 않습니다.".formatted(id))
        );
    }

    @Operation(summary = "게시글 저장", description = "게시글을 저장합니다.")
    @PostMapping(value = "", consumes = "multipart/form-data; charset=UTF-8")
    @Transactional
    public RsData< DreamBoardResponse > insertDreamBoard(
        @RequestHeader(name = "accessToken", required = false) String accessToken,
        @ModelAttribute DreamBoardWriteRequest writeRequest,
        @RequestParam(name = "file", required = false) List<MultipartFile> files,
        HttpServletRequest request
    ) {
        log.info("게시글 저장 실행 token => {}, dto => {}", accessToken, writeRequest);
        if (accessToken == null) {
            return RsData.of("F-5", "header에 jwt토큰을 담아주세요.");
        }
        if (!jwtUtil.validateToken(accessToken)){
            return RsData.of("F-6", "토큰만료");
        }

        Long userId = jwtUtil.getId(accessToken);
        Optional<UserEntity> userEntity = userRepository.findById(userId);
        Optional<DreamBoardCategoryEntity> categoryEntity = dreamBoardCategoryRepository.findById(writeRequest.getCategoryFk());
        if(!userEntity.isPresent() || !categoryEntity.isPresent()){
            return RsData.of("F-7", "유효한 요청이 아닙니다.");
        }
        
        DreamBoardEntity entity = DreamBoardEntity.builder()
                                    .user(userEntity.get())
                                    .category(categoryEntity.get())
                                    .title(writeRequest.getTitle()).content(writeRequest.getContent())
                                    .tag1(writeRequest.getTag1()).tag2(writeRequest.getTag2()).tag3(writeRequest.getTag3())
                                    .targetPrice(writeRequest.getTargetPrice())
                                    .startDate(writeRequest.getStartDate()).endDate(writeRequest.getEndDate())
                                    .ip(request.getRemoteAddr()).build();

        String uploadPath = request.getServletContext().getRealPath("/upload/");
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
                        
                        if(isFirstFile){ // 첫번째 사진인경우 썸네일로 저장
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
            } catch (IOException e) {
                e.printStackTrace();
                return RsData.of("F-10", "저장중 문제 발생");
            }
        } else {
            return RsData.of("F-10", "저장중 문제 발생");
        }
        
        entityManager.clear(); // 영속성 컨테스트 지우기
        DreamBoardEntity entity2 = boardService.findById(entity.getId()).get();
        return RsData.of("S-2", "게시글 저장 성공", new DreamBoardResponse(entity2));
    }

    /*
     * Todo 수정은 이미 저장되어있는 사진은 안건드리는 로직을 추가해야한다.
     */
    @Operation(summary = "게시글 수정", description = "지정된 id의 게시글을 수정합니다.")
    @PutMapping("/{id}")
    public RsData< DreamBoardResponse > updateDreamBoard(
        // @RequestHeader(name = "accessToken") String accessToken,
        @PathVariable(name = "id") Long id,
        @ModelAttribute DreamBoardUpdateRequest updateRequest,
        @RequestParam(name = "file", required = false) List<MultipartFile> files,
        HttpServletRequest request
    ){
        DreamBoardEntity entity = null;
        boolean result = false; // boardService.saveDreamBoard(entity, files);
        // boolean result = boardService.saveDreamBoard(entity, files);
        if(result) {
            return RsData.of("S-3", "게시글 수정 성공", new DreamBoardResponse(entity));
        } else {
            return RsData.of("F-3", "게시물 수정 실패");
        }
    }


    @Data
    public static class RemoveResponse{
        private final DreamBoardEntity dreamBoardEntity;
    }

    // 게시글을 삭제하는 주소
    @Operation(summary = "게시글 삭제", description = "지정된 id의 게시글을 삭제합니다.")
    @DeleteMapping("/{id}")
    public RsData< DreamBoardResponse > deleteDreamBoardByIdx(
        // @RequestHeader(name = "accessToken") String accessToken,
        @PathVariable(name = "id") Long id
    ){
        Optional<DreamBoardEntity> optionalEntity = boardService.findById(id);

        if(optionalEntity.isEmpty()){
            return RsData.of("F-4", "%d번 게시물은 존재하지 않습니다.".formatted(id));
        }
        DreamBoardEntity entity = optionalEntity.get();
        boolean result = boardService.deleteById(id);

        if(result) {
            return RsData.of("S-4", "게시글 삭제 성공", new DreamBoardResponse(entity));
        } else {
            return RsData.of("F-4", "게시물 삭제 실패");
        }
    }

}
