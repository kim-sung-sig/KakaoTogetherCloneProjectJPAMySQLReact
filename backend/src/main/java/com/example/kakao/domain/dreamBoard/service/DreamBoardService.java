package com.example.kakao.domain.dreamboard.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.example.kakao.domain.dreamboard.dto.request.DreamBoardUpdateRequest;
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

import jakarta.servlet.http.HttpServletRequest;
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

    // 주입
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

    /**
     * 게시글 목록 얻기
     * @param lastId
     * @param size
     * @param categoryId
     * @param search
     * @return
     * @throws Exception
     */
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

    /**
     * 게시글 하나 얻기(상세보기)
     * @param id
     * @return
     * @throws EntityNotFoundException
     */
    public DreamBoardResponse findById(Long id) throws EntityNotFoundException {
        Optional<DreamBoardEntity> optional = dreamBoardRepository.findById(id);
        if(optional.isPresent()){
            return new DreamBoardResponse(optional.get());
        } else {
            throw new EntityNotFoundException("id에 해당하는 게시글이 존재하지 않습니다.");
        }
    }

    /**
     * 게시글 저장하기
     * @param accessToken
     * @param uploadRequest
     * @param req
     * @return
     * @throws Exception
     */
    @Transactional
    public Boolean saveDreamBoard(String accessToken, DreamBoardUploadRequest uploadRequest, HttpServletRequest req) throws Exception{
        try {
            // 검증 시작
            Long userId = jwtUtil.getId(accessToken);
            UserEntity dbUser = userRepository.findById(userId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "탈퇴한 회원 Or 이상한 회원"));

            DreamBoardCategoryEntity dbCategory = dreamBoardCategoryRepository.findById(uploadRequest.getCategoryNum())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "카테고리번호 이상"));

            if (uploadRequest.getStartDate().isAfter(uploadRequest.getEndDate())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "시작 날짜는 종료 날짜보다 앞서야 합니다.");
            }

            List<MultipartFile> files = uploadRequest.getFile();
            if (files == null || files.isEmpty() || files.size() > 5) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "파일은 최소 1개 이상, 최대 5개 이하이어야 합니다.");
            } // 검증 끝

            // 저장 시작
            // Entity 생성
            DreamBoardEntity board = DreamBoardEntity.builder()
                    .user(dbUser)
                    .category(dbCategory)
                    .title(uploadRequest.getTitle())
                    .content(uploadRequest.getContent())
                    .tag1(uploadRequest.getTag1())
                    .tag2(uploadRequest.getTag2())
                    .tag3(uploadRequest.getTag3())
                    .targetPrice(uploadRequest.getTargetPrice())
                    .startDate(uploadRequest.getStartDate())
                    .endDate(uploadRequest.getEndDate())
                    .ip(req.getRemoteAddr())
                    .fileEntities(new ArrayList<>()) // 사진저장후 addAll()
                    .build();

            // 사진 저장 준비
            String uploadPath = req.getServletContext().getRealPath(fileDirPath);
            log.info("uploadPath => {}", uploadPath);

            try {
                File uploadDir = new File(uploadPath);
                if (!uploadDir.exists()) {
                    uploadDir.mkdirs();
                }

                List<DreamBoardFileEntity> fileEntities = new ArrayList<>();
                int count = 1;
                for (MultipartFile file : files) {
                    if (file != null && file.getSize() > 0) {
                        String saveFileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
                        File saveFile = new File(uploadDir, saveFileName);
                        FileCopyUtils.copy(file.getBytes(), saveFile);

                        DreamBoardFileEntity fileEntity = DreamBoardFileEntity.builder()
                                .board(board)
                                .saveFileName(saveFileName)
                                .orders(count++)
                                .build();

                        fileEntities.add(fileEntity);
                    }
                }

                if (!fileEntities.isEmpty()) {
                    board.setThumbnail(fileEntities.get(0).getSaveFileName()); // 썸네일
                    board.getFileEntities().addAll(fileEntities);
                    dreamBoardRepository.save(board);
                } else {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "유효한 파일이 없습니다.");
                }

            } catch (IOException e) {
                log.debug("사진 저장중 에러가 발생했습니다.", e);
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "사진 저장중 에러");
            }

        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error occurred while saving dream board", e);
            throw new Exception();
        }

        return true;
    }

    @Transactional
    public boolean updateDreamBoard(String accessToken, Long boardId, DreamBoardUpdateRequest updateRequest, HttpServletRequest req) throws Exception {
        try {
            // 검증 시작
            Long userId = jwtUtil.getId(accessToken);

            // 존재하는 게시글
            DreamBoardEntity dbBoard = dreamBoardRepository.findById(boardId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "not id"));

            // 카테고리
            DreamBoardCategoryEntity categoryEntity = dreamBoardCategoryRepository.findById(updateRequest.getCategoryNum())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,"카테고리번호 이상"));
            
            // 유저가 맞는지 + 자신이 쓴 글이 맞는지
            userRepository.findById(userId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "탈퇴한 회원 Or 이상한 회원"));
            Optional.ofNullable(dbBoard)
                    .map(board -> board.getUser().getId())
                    .filter(id -> id.equals(userId)) // TODO 나중에 관리자는 가능하게?
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "해당유저가 쓴글이 아님"));

            // 새로운 값을 넣어주자
            dbBoard.setCategory(categoryEntity);
            dbBoard.setTitle(updateRequest.getTitle());
            dbBoard.setContent(updateRequest.getContent());
            dbBoard.setTag1(updateRequest.getTag1());
            dbBoard.setTag2(updateRequest.getTag2());
            dbBoard.setTag3(updateRequest.getTag3());
            dbBoard.setTargetPrice(updateRequest.getTargetPrice() <= dbBoard.getCurrentPrice() ? dbBoard.getCurrentPrice() : updateRequest.getTargetPrice());
            dbBoard.setEndDate(updateRequest.getEndDate());
            dbBoard.setIp(req.getRemoteAddr());

            // 저장 실행!
            String uploadPath = req.getServletContext().getRealPath(fileDirPath);
            log.info("uploadPath => {}", uploadPath);

            Map<Long, Integer> fileOrderMap = updateRequest.getFileOrderMap();
            List<MultipartFile> files = updateRequest.getFile(); // 새로 추가된 사진들
            try {
                // 저장 실행!
                File uploadDir = new File(uploadPath);
                if(!uploadDir.exists()){
                    uploadDir.mkdirs();
                }

                // 삭제된 사진이 있는지 확인 및 파일 처리
                List<DreamBoardFileEntity> newFileEntities = new ArrayList<>();
                List<Long> preFileEntityIds = dreamBoardFileRepository.findIdByBoardId(dbBoard.getId());
                String thumnail = null;
                for (Long fileId : preFileEntityIds) {
                    if (!fileOrderMap.containsKey(fileId)) {
                        DreamBoardFileEntity deleteFileEntity = dreamBoardFileRepository.findById(fileId)
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "왠지 몰겟네? 파일 삭제하려함"));
                        File deleteFile = new File(uploadDir, deleteFileEntity.getSaveFileName());
                        if (deleteFile.exists()) {
                            deleteFile.delete();
                        }
                    } else {
                        DreamBoardFileEntity saveFileEntity = dreamBoardFileRepository.findById(fileId)
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "음! 일단 이거는 발생안함"));
                        Integer newOrder = fileOrderMap.get(fileId);
                        if(newOrder.equals(1)) thumnail = saveFileEntity.getSaveFileName();
                        saveFileEntity.setOrders(newOrder);
                        newFileEntities.add(saveFileEntity);
                    }
                }

                // 새로운 파일 저장 및 기존 파일 순서 변경
                long count = 1;
                for (MultipartFile file : files) {
                    String saveFileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
                    File saveFile = new File(uploadDir, saveFileName);
                    FileCopyUtils.copy(file.getBytes(), saveFile);

                    Integer order = fileOrderMap.get(count * -1L);
                    if(order.equals(1)) thumnail = saveFileName;

                    DreamBoardFileEntity fileEntity = DreamBoardFileEntity.builder()
                            .board(dbBoard)
                            .saveFileName(saveFileName)
                            .orders(order)
                            .build();

                    newFileEntities.add(fileEntity);
                    count++;
                }

                dbBoard.setThumbnail(thumnail); // 썸네일
                dbBoard.getFileEntities().addAll(newFileEntities);
                dreamBoardRepository.save(dbBoard);

            } catch (IOException e) {
                throw new Exception("사진 저장중 에러발생");
            }
        } catch (ResponseStatusException e){
            throw e;
        } catch (Exception e){
            log.error("Unexpected error occurred while saving dream board", e);
            throw new Exception();
        }
        return true;
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
