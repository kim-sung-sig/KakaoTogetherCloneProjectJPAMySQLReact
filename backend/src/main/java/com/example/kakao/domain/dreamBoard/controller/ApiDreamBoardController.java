package com.example.kakao.domain.dreamBoard.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.example.kakao.DTO.request.ScrollRequest;
import com.example.kakao.domain.dreamBoard.entity.DreamBoardEntity;
import com.example.kakao.domain.dreamBoard.service.DreamBoardService;
import com.example.kakao.global.RsData.RsData;

import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@RestController
@RequestMapping("/api/v1/dreamBoards")
public class ApiDreamBoardController {

    // @Autowired
    // private JWTUtil jwtUtil;
    @Autowired
    private DreamBoardService boardService;

    // 다건 객체
    @AllArgsConstructor
    @Getter
    public static class DreamBoardsResponse {
        private final List<DreamBoardEntity> dreamBoardEntities;
    }

    // 단건 객체
    @AllArgsConstructor
    @Getter
    public static class DreamBoardResponse {
        private final DreamBoardEntity dreamBoardEntity;
    }


    @Operation(summary = "게시글 다건 조회", description = "지정된 id에 해당하는 게시글을 조회합니다.")
    @GetMapping(value = {"", "/"})
    public RsData<DreamBoardsResponse> getBoardList(@ModelAttribute ScrollRequest sc){
        List<DreamBoardEntity> list = boardService.getList();
        return RsData.of("S-1", "success", new DreamBoardsResponse(list));
    }

    @Operation(summary = "게시글 단건 조회", description = "지정된 id에 해당하는 게시글을 조회합니다.")
    @GetMapping("/{id}")
    public RsData<DreamBoardResponse> getDreamBoardById(@PathVariable(name = "id") Long id) {
        return boardService.getDreamBoardById(id).map((board) -> 
            RsData.of("S-1", "success", new DreamBoardResponse(board))
        ).orElseGet(() ->
            RsData.of("F-1", "%d th data not exist".formatted(id))
        );
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class WriteRequest {
        private Long categoryFk;
        private String title;
        private String content;
        private String tag1;
        private String tag2;
        private String tag3;
        private Integer targetPrice;
        private String startDate; // 문자로 받고 LDT로 바꾸자
        private String endDate; // 문자로 받고 LDT로 바꾸자
    }

    @Operation(summary = "게시글 저장", description = "게시글을 저장합니다.")
    // @PostMapping(value = {"", "/"}, consumes = "multipart/form-data; charset=UTF-8")
    @PostMapping(value = {"", "/"}, consumes = "multipart/form-data") // postman 용
    public void insertDreamBoard(
        // @RequestHeader(name = "access") String accessToken,
        @ModelAttribute WriteRequest writeRequest,
        @RequestParam("file") List<MultipartFile> files
    ) {
        System.out.println(writeRequest);

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
    @Operation(summary = "게시글 수정", description = "지정된 id의 게시글을 수정합니다.")
    @PutMapping("/{id}")
    public ResponseEntity<Boolean> updateDreamBoard(@PathVariable(name = "id") Long id){
        return ResponseEntity.ok().body(true);
    }

    /**
     * 게시글을 삭제하는 주소
     * @param id
     * @return
     */
    @Operation(summary = "게시글 삭제", description = "지정된 id의 게시글을 삭제합니다.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteDreamBoardByIdx(@PathVariable(name = "id") Long id){
        return ResponseEntity.ok().body(true);
    }

}
