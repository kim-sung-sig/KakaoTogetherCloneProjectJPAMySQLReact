package com.example.kakao.domain.dreamboard.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.kakao.domain.dreamboard.entity.DreamBoardCommentEntity;
import com.example.kakao.domain.dreamboard.entity.DreamBoardEntity;


@Repository
public interface DreamBoardCommentRepository extends JpaRepository<DreamBoardCommentEntity, Long> {
    
    // 1. 다건조회 (board에 해당하는 이것도 페이징 처리 해야됨)
    List<DreamBoardCommentEntity> findByBoard(DreamBoardEntity board);
    
    // 2. 단건조회
    // Optional<DreamBoardCommentEntity> findById(Long id);
    
    // 3. 저장

    // 4. 수정

    // 5. 삭제

    // 6. 내가 쓴 댓글

    
}
