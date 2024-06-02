package com.example.kakao.domain.dreamboard.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.kakao.domain.dreamboard.dto.request.DreamBoardUpdateRequest;
import com.example.kakao.domain.dreamboard.dto.request.DreamBoardUploadRequest;
import com.example.kakao.domain.dreamboard.dto.response.DreamBoardResponse;
import com.example.kakao.domain.dreamboard.service.DreamBoardService;
import com.example.kakao.global.dto.request.ScrollRequest;
import com.example.kakao.global.dto.response.PagingResponse;
import com.example.kakao.global.dto.response.RsData;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/dreamBoards")
@RequiredArgsConstructor
public class ApiDreamBoardController {

    //@Autowired
    private final DreamBoardService boardService;


    @Operation(summary = "게시글 다건 조회")
    @GetMapping(value = "")
    public ResponseEntity<RsData< PagingResponse<DreamBoardResponse> >> getDreamBoardList(HttpServletRequest request, @ModelAttribute ScrollRequest sc){
        PagingResponse<DreamBoardResponse> data = null;
        try {
            data = boardService.findAllByCondition(sc.getLastItemId(), sc.getSize(), sc.getCategoryNum(), sc.getSearch());

        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(RsData.of(e.getMessage()), e.getStatusCode());
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(RsData.of("ok", data), HttpStatus.OK);
    }


    @Operation(summary = "게시글 단건 조회", description = "지정된 id에 해당하는 게시글을 조회합니다.")
    @GetMapping("/{id}")
    public ResponseEntity< RsData<DreamBoardResponse> > getDreamBoardById(@PathVariable(name = "id") Long id) {
        DreamBoardResponse data = null;
        try {
            data = boardService.findById(id);
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(RsData.of(e.getMessage()), e.getStatusCode());
        } catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(RsData.of("ok", data), HttpStatus.OK);
    }


    @Operation(summary = "게시글 저장", description = "게시글을 저장합니다.")
    @PostMapping(value = "", consumes = "multipart/form-data; charset=UTF-8")
    public ResponseEntity< RsData<Boolean> > insertDreamBoard(
        @RequestHeader(name = "Authorization") String authorization,
        @ModelAttribute @Valid DreamBoardUploadRequest uploadRequest,
        HttpServletRequest req
    ) {
        boolean result = false;
        String accessToken = authorization.split(" ")[1]; // accessToken 추출
        try {
            result = boardService.saveDreamBoard(accessToken, uploadRequest, req);

        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(RsData.of(e.getMessage()), e.getStatusCode());
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(RsData.of(result), HttpStatus.OK);
    }


    @Operation(summary = "게시글 수정", description = "지정된 id의 게시글을 수정합니다.")
    @PutMapping(value = "/{id}", consumes = "multipart/form-data; charset=UTF-8")
    public ResponseEntity< RsData<Boolean> > updateDreamBoard(
        @RequestHeader(name = "Authorization", required = true) String authorization,
        @PathVariable(name = "id") Long id,
        @ModelAttribute @Valid DreamBoardUpdateRequest updateRequest,
        HttpServletRequest request
    ){
        boolean result = false;
        String accessToken = authorization.split(" ")[1]; // accessToken 추출
        try {
            result = boardService.updateDreamBoard(accessToken, id, updateRequest, request);

        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(RsData.of(e.getMessage()), e.getStatusCode());
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(RsData.of(result), HttpStatus.OK);
    }


    @Operation(summary = "게시글 삭제", description = "지정된 id의 게시글을 삭제합니다.")
    @DeleteMapping("/{id}")
    public ResponseEntity< RsData<Boolean> > deleteDreamBoardByIdx(
        @RequestHeader(name = "Authorization") String authorization,
        @PathVariable(name = "id") Long id,
        HttpServletRequest request
    ){
        boolean result = false;
        String accessToken = authorization.split(" ")[1]; // accessToken 추출
        try {
            result = boardService.deleteById(accessToken, id, request);

        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(RsData.of(e.getMessage()), e.getStatusCode());
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(RsData.of(result), HttpStatus.OK);
    }

}
