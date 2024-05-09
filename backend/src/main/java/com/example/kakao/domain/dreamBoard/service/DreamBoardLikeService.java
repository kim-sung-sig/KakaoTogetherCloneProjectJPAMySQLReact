package com.example.kakao.domain.dreamboard.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.kakao.domain.dreamboard.entity.DreamBoardEntity;
import com.example.kakao.domain.dreamboard.entity.DreamBoardLikeEntity;
import com.example.kakao.domain.dreamboard.repository.DreamBoardLikeRepository;
import com.example.kakao.domain.dreamboard.repository.DreamBoardRepository;
import com.example.kakao.domain.user.entity.UserEntity;
import com.example.kakao.domain.user.repository.UserRepository;

@Service("dreamBoardLikeService")
public class DreamBoardLikeService {

    @Autowired
    private DreamBoardRepository dreamBoardRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DreamBoardLikeRepository dreamBoardLikeRepository;

    // 좋아요 확인
    public boolean hasLiked(Long boardId, Long userId) {
        return dreamBoardLikeRepository.findByBoardAndUser(boardId, userId).isPresent();
    }


    // 좋아요 하기
    public boolean insertLike(Long boardId, Long userId) {
        Optional<DreamBoardEntity> boardEntity = dreamBoardRepository.findById(boardId);
        Optional<UserEntity> userEntity = userRepository.findById(userId);
    
        if (boardEntity.isPresent() && userEntity.isPresent()) { // 둘다 있는 경우
            DreamBoardLikeEntity likeEntity = DreamBoardLikeEntity.builder()
                                                .board(boardEntity.get())
                                                .user(userEntity.get())
                                                .build();
            dreamBoardLikeRepository.save(likeEntity);
            return true;
        } else {
            return false;
        }
    }
    
    // 좋아요 취소하기
    public boolean deleteLike(Long boardId, Long userId){
        Optional<DreamBoardEntity> boardEntity = dreamBoardRepository.findById(boardId);
        Optional<UserEntity> userEntity = userRepository.findById(userId);
    
        if (boardEntity.isPresent() && userEntity.isPresent()) { // 둘다 있는 경우
            dreamBoardLikeRepository.deleteByBoardAndUser(boardId, userId);
            return true;
        } else {
            return false;
        }
    }
}
