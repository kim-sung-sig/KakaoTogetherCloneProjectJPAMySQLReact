package com.example.kakao.domain.dreamboard.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.example.kakao.domain.dreamboard.dto.request.DreamBoardCommentRequest;
import com.example.kakao.domain.dreamboard.dto.response.DreamBoardCommentResponse;
import com.example.kakao.domain.dreamboard.entity.DreamBoardCommentEntity;
import com.example.kakao.domain.dreamboard.entity.DreamBoardEntity;
import com.example.kakao.domain.dreamboard.entity.QDreamBoardCommentEntity;
import com.example.kakao.domain.dreamboard.repository.DreamBoardCommentRepository;
import com.example.kakao.domain.dreamboard.repository.DreamBoardRepository;
import com.example.kakao.domain.user.entity.QUserEntity;
import com.example.kakao.domain.user.entity.UserEntity;
import com.example.kakao.domain.user.repository.UserRepository;
import com.example.kakao.global.dto.response.PagingResponse;
import com.example.kakao.global.security.jwt.util.JWTUtil;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class DreamBoardCommentService {

    // @Autowired
    private final JWTUtil jwtUtil;
    private final UserRepository userRepository;
    private final DreamBoardRepository dreamBoardRepository;
    private final DreamBoardCommentRepository dreamBoardCommentRepository;
    private final JPAQueryFactory jpaQueryFactory;


    // 1. 댓글 목록얻기
    /**
     * 댓글 목록얻기
     * @param boardId
     * @param lastId
     * @param size
     * @return
     * @throws Exception
     */
    public PagingResponse<DreamBoardCommentResponse> findAllByBoardIdAndCondition(Long boardId, Long lastId, int size) throws Exception{
        // 유효성검사 // TODO 나중에 컨디션도 추가해보자
        if((lastId != null && lastId < 0) || size < 0){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "유효하지않은 lastId or size");
        }
        dreamBoardRepository.findById(boardId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "not id"));
        
        // 추출시작
        QUserEntity userEntity = QUserEntity.userEntity;
        QDreamBoardCommentEntity dreamBoardCommentEntity = QDreamBoardCommentEntity.dreamBoardCommentEntity;

        Pageable pageable = PageRequest.of(0, size);

        JPAQuery<DreamBoardCommentEntity> jpaQuery = jpaQueryFactory
            .select(dreamBoardCommentEntity)
            .from(dreamBoardCommentEntity)
            .leftJoin(dreamBoardCommentEntity.user, userEntity).fetchJoin()
            .where(
                lastId != null ? dreamBoardCommentEntity.id.lt(lastId) : null,
                dreamBoardCommentEntity.board.id.eq(boardId)
            )
            .orderBy(dreamBoardCommentEntity.id.desc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize());
        
        JPAQuery<Long> jpaQueryCount = jpaQueryFactory
            .select(dreamBoardCommentEntity.count())
            .from(dreamBoardCommentEntity)
            .where(
                dreamBoardCommentEntity.board.id.eq(boardId)
            );
        
        // 리턴
        List<DreamBoardCommentEntity> resultList = jpaQuery.fetch();
        Long totalCount = jpaQueryCount.fetchOne();

        PagingResponse<DreamBoardCommentResponse> response = new PagingResponse<>();
        response.setList(resultList.stream().map((e) -> new DreamBoardCommentResponse(e)).collect(Collectors.toList()));
        response.setTotalCount(totalCount);
        response.setHasNext(resultList.size() == pageable.getPageSize());
        response.setLastId(!resultList.isEmpty() ? resultList.get(resultList.size() - 1).getId() : null);
        return response;
    }


    // 2. 댓글 한개얻기
    /**
     * 댓글 한개얻기
     * @param id
     * @return
     * @throws Exception
     */
    public DreamBoardCommentResponse findByBoardIdAndId(Long boardId, Long commentId) throws Exception{
        DreamBoardEntity boardEntity = dreamBoardRepository.findById(boardId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "게시글 not id"));
        DreamBoardCommentEntity commmentEntity = dreamBoardCommentRepository.findById(commentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "not id"));
        if(!boardEntity.getCommentEntitys().contains(commmentEntity)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "not match");
        }
        return new DreamBoardCommentResponse(commmentEntity);
    }


    // 3. 댓글 쓰기 //TODO 댓글저장할때 donation 생성하고 board에 donation 100원 적립해주기
    /**
     * 댓글 쓰기
     * @param accessToken
     * @param boardId
     * @param uploadRequest
     * @param req
     * @return
     * @throws Exception
     */
    @Transactional
    public Boolean saveDreamBoardComment(
        String accessToken, Long boardId,
        DreamBoardCommentRequest uploadRequest, HttpServletRequest req
    ) throws Exception{

        try {
            // 검증 시작
            Long userId = jwtUtil.getId(accessToken);
            UserEntity dbUser = userRepository.findById(userId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "탈퇴한 회원 Or 이상한 회원"));
            DreamBoardEntity dbDreamBoard = dreamBoardRepository.findById(boardId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "게시글 not id"));

            DreamBoardCommentEntity comment = DreamBoardCommentEntity.builder()
                    .user(dbUser)
                    .board(dbDreamBoard)
                    .comment(uploadRequest.getComment())
                    .ip(req.getRemoteAddr())
                    .build();
            
            dreamBoardCommentRepository.save(comment);
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error occurred while saving dream board", e);
            throw new Exception();
        }
        return true;
    }


    // 4. 댓글 수정
    /**
     * 댓글 수정
     * @param accessToken
     * @param boardId
     * @param commentId
     * @param updateRequest
     * @param req
     * @return
     * @throws Exception
     */
    @Transactional
    public Boolean updateDreamBoard(
        String accessToken, Long boardId, Long commentId,
        DreamBoardCommentRequest updateRequest, HttpServletRequest req
    ) throws Exception {

        try {
            // 검증 시작
            Long userId = jwtUtil.getId(accessToken);
            // 존재하는 게시글
            dreamBoardRepository.findById(boardId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "게시글 not id"));
            // 존재하는 댓글
            DreamBoardCommentEntity dbDreamBoardComment = dreamBoardCommentRepository.findById(commentId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "not id"));
            // 유저가 맞는지 + 자신이 쓴 글이 맞는지
            userRepository.findById(userId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "탈퇴한 회원 Or 이상한 회원"));
            Optional.ofNullable(dbDreamBoardComment)
                    .map(comment -> comment.getUser().getId())
                    .filter(id -> id.equals(userId)) // TODO 나중에 관리자는 가능하게?
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "해당유저가 쓴글이 아님"));
            
            dbDreamBoardComment.setComment(updateRequest.getComment());
            dbDreamBoardComment.setIp(req.getRemoteAddr());

            dreamBoardCommentRepository.save(dbDreamBoardComment);
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error occurred while saving dream board", e);
            throw new Exception();
        }
        return true;
    }


    // 5. 댓글 삭제 //TODO 진짜 삭제 말고 숨김표시로 하자 진짜삭제는 따로만들자
    /**
     * 댓글 삭제
     * @param accessToken
     * @param boardId
     * @param commentId
     * @return
     * @throws Exception
     */
    @Transactional
    public Boolean deleteById(String accessToken, Long boardId, Long commentId) throws Exception {
        try {
            // 검증 시작
            Long userId = jwtUtil.getId(accessToken);
            // 존재하는 게시글
            dreamBoardRepository.findById(boardId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "게시글 not id"));
            // 존재하는 댓글
            DreamBoardCommentEntity dbDreamBoardComment = dreamBoardCommentRepository.findById(commentId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "not id"));
            // 유저가 맞는지 + 자신이 쓴 글이 맞는지
            userRepository.findById(userId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "탈퇴한 회원 Or 이상한 회원"));
            Optional.ofNullable(dbDreamBoardComment)
                    .map(comment -> comment.getUser().getId())
                    .filter(id -> id.equals(userId)) // TODO 나중에 관리자는 가능하게?
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "해당유저가 쓴글이 아님"));
            
            dreamBoardCommentRepository.delete(dbDreamBoardComment);
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error occurred while saving dream board", e);
            throw new Exception();
        }
        return true;
    }

}
