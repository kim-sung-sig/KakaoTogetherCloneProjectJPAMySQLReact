package com.example.kakao.domain.dreamboard.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.kakao.domain.dreamboard.dto.request.DreamBoardUpdateRequest;
import com.example.kakao.domain.dreamboard.dto.request.DreamBoardWriteRequest;
import com.example.kakao.domain.dreamboard.dto.response.DreamBoardResponse;
import com.example.kakao.domain.dreamboard.entity.DreamBoardCategoryEntity;
import com.example.kakao.domain.dreamboard.entity.DreamBoardEntity;
import com.example.kakao.domain.dreamboard.service.DreamBoardService;
import com.example.kakao.domain.user.entity.UserEntity;
import com.example.kakao.global.DTO.request.ScrollRequest;
import com.example.kakao.global.RsData.RsData;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.persistence.EntityManager;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;

@RestController
@RequestMapping("/api/v1/dreamBoards")
public class ApiDreamBoardController {

    // @Autowired
    // private JWTUtil jwtUtil;
    @Autowired
    private DreamBoardService boardService;
    @Autowired
    private EntityManager entityManager;

    @Operation(summary = "게시글 다건 조회", description = "지정된 id에 해당하는 게시글을 조회합니다.")
    @GetMapping(value = "")
    public RsData< List<DreamBoardResponse> > getBoardList(@ModelAttribute ScrollRequest sc){
        List<DreamBoardEntity> list = boardService.getList();
        List<DreamBoardResponse> resultList = null;
        if(list != null && list.size() != 0){
            resultList = list.stream()
            .map(entity -> new DreamBoardResponse(entity))
            .collect(Collectors.toList());
        }
        System.out.println(resultList);
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
    public RsData< DreamBoardResponse > insertDreamBoard(
        // @RequestHeader(name = "access") String accessToken,
        @ModelAttribute DreamBoardWriteRequest writeRequest,
        @RequestParam(name = "file", required = false) List<MultipartFile> files,
        HttpServletRequest request
    ) {
        DreamBoardEntity entity = DreamBoardEntity.builder()
                                    .user(UserEntity.builder().id(1L).build())
                                    .category(DreamBoardCategoryEntity.builder().id(writeRequest.getCategoryFk()).build())
                                    .title(writeRequest.getTitle()).content(writeRequest.getContent())
                                    .tag1(writeRequest.getTag1()).tag2(writeRequest.getTag2()).tag3(writeRequest.getTag3())
                                    .targetPrice(writeRequest.getTargetPrice())
                                    .startDate(writeRequest.getStartDate()).endDate(writeRequest.getEndDate())
                                    .ip(request.getRemoteAddr()).build();

        String uploadPath = request.getServletContext().getRealPath("/upload/");
        boolean result = boardService.saveDreamBoard(entity, files, uploadPath);
        if(result) {
            entityManager.clear(); // 영속성 컨테스트 지우기
            DreamBoardEntity entity2 = boardService.findById(entity.getId()).get();
            return RsData.of("S-2", "게시글 저장 성공", new DreamBoardResponse(entity2));
        } else {
            return RsData.of("F-2", "게시물 저장 실패");
        }

        /*
        if (accessToken == null || !jwtUtil.validateToken(accessToken)) {
            return ResponseEntity.badRequest().body(new StatusDTO(HttpStatus.BAD_REQUEST.value(), "Invalid accessToken"));
        }
        // boolean result = boardService.saveDreamBoard(dto, request);
        boolean result = false;
        if (result) {
            return ResponseEntity.ok().body(new StatusDTO(HttpStatus.OK.value(), "success"));
        } else {
            return ResponseEntity.badRequest().body(new StatusDTO(HttpStatus.BAD_REQUEST.value(), "Failed to save DreamBoard"));
        }
         */
    }

    /*
     * Todo 수정은 이미 저장되어있는 사진은 안건드리는 로직을 추가해야한다.
     */
    @Operation(summary = "게시글 수정", description = "지정된 id의 게시글을 수정합니다.")
    @PutMapping("/{id}")
    public RsData< DreamBoardResponse > updateDreamBoard(
        // @RequestHeader(name = "access") String accessToken,
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
        // @RequestHeader(name = "access") String accessToken,
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
