package com.example.kakao.domain.dreamboard.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.kakao.domain.dreamboard.entity.DreamBoardEntity;
import com.example.kakao.domain.dreamboard.entity.DreamBoardLikeEntity;
import com.example.kakao.domain.dreamboard.repository.DreamBoardLikeRepository;
import com.example.kakao.domain.dreamboard.repository.DreamBoardRepository;
import com.example.kakao.domain.user.entity.UserEntity;
import com.example.kakao.domain.user.repository.UserRepository;
import com.example.kakao.global.security.jwt.util.JWTUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service("dreamBoardLikeService")
@RequiredArgsConstructor
@Slf4j
public class DreamBoardLikeService {

    // @Autowired
    private final DreamBoardRepository dreamBoardRepository;
    private final UserRepository userRepository;
    private final DreamBoardLikeRepository dreamBoardLikeRepository;
    private final JWTUtil jwtUtil;


    /**
     * 좋아요 확인
     * @param boardId
     * @param userId
     * @return
     */
    public boolean hasLiked(String accessToken, Long boardId) throws Exception{
        boolean result = false;
        try {
            Long userId = jwtUtil.getId(accessToken);
            userRepository.findById(userId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "탈퇴한 회원 Or 이상한 회원"));
            dreamBoardRepository.findById(boardId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "not Id"));

            result = dreamBoardLikeRepository.findByBoardAndUser(boardId, userId).isPresent();
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error occurred while saving dream board", e);
            throw new Exception();
        }

        return result;
    }


    /**
     * 좋아요하기
     * @param boardId
     * @param userId
     * @return
     */
    public boolean insertLike(String accessToken, Long boardId) throws Exception{
        try{
            Long userId = jwtUtil.getId(accessToken);
            UserEntity dbUser = userRepository.findById(userId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "탈퇴한 회원 Or 이상한 회원"));
            DreamBoardEntity boardEntity = dreamBoardRepository.findById(boardId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "not Id"));

            DreamBoardLikeEntity likeEntity = DreamBoardLikeEntity.builder()
                    .board(boardEntity)
                    .user(dbUser)
                    .build();
            dreamBoardLikeRepository.save(likeEntity);
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error occurred while saving dream board", e);
            throw new Exception();
        }
        return true;
    }


    /**
     * 좋아요 취소하기
     * @param boardId
     * @param userId
     * @return
     */
    public boolean deleteLike(String accessToken, Long boardId) throws Exception{
        try{
            Long userId = jwtUtil.getId(accessToken);
            userRepository.findById(userId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "탈퇴한 회원 Or 이상한 회원"));
            dreamBoardRepository.findById(boardId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "not Id"));

            dreamBoardLikeRepository.deleteByBoardAndUser(boardId, userId);
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error occurred while saving dream board", e);
            throw new Exception();
        }
        return true;
    }
}
