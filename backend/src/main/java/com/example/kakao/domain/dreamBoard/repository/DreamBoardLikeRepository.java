package com.example.kakao.domain.dreamboard.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.kakao.domain.dreamboard.entity.DreamBoardLikeEntity;

@Repository
public interface DreamBoardLikeRepository extends JpaRepository<DreamBoardLikeEntity, Long>{
    
    // 1. 좋아요 있는지 확인
    @Query("SELECT dbl FROM DreamBoardLikeEntity dbl WHERE dbl.board.id = :boardId AND dbl.user.id = :userId")
    Optional<DreamBoardLikeEntity> findByBoardAndUser(@Param("boardId") Long boardId, @Param("userId") Long userId);

    // 2. 삭제
    @Modifying
    @Query("DELETE FROM DreamBoardLikeEntity dbl WHERE dbl.board.id = :boardId AND dbl.user.id = :userId")
    void deleteByBoardAndUser(@Param("boardId") Long boardId, @Param("userId") Long userId);

}
