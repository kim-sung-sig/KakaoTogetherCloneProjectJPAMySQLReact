package com.example.kakao.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.kakao.entity.DreamBoardEntity;


@Repository
public interface DreamBoardRepository extends JpaRepository<DreamBoardEntity, Long> {
    
    Page<DreamBoardEntity> findByIdxLessThanOrderByIdxDesc(Long lastItemIdx, Pageable pageable);
    Optional<DreamBoardEntity> findByIdx(Long idx);

    @Query("UPDATE DreamBoardEntity d SET d.readCount = d.readCount + 1 WHERE d.idx = :idx")
    void incrementViewCount(@Param("idx") Long idx);

    @Query("UPDATE DreamBoardEntity d SET d.likeCount = d.likeCount + 1 WHERE d.idx = :idx")
    void incrementLikeCount(@Param("idx") Long idx);

    @Query("UPDATE DreamBoardEntity d SET d.likeCount = d.likeCount - 1 WHERE d.idx = :idx")
    void decrementLikeCount(@Param("idx") Long idx);

    @Query("UPDATE DreamBoardEntity d SET d.commentCount = d.commentCount + 1 WHERE d.idx = :idx")
    void incrementCommentCount(@Param("idx") Long idx);

    @Query("UPDATE DreamBoardEntity d SET d.commentCount = d.commentCount - 1 WHERE d.idx = :idx")
    void decrementCommentCount(@Param("idx") Long idx);

    @Query("UPDATE DreamBoardEntity d SET d.donateCount = d.donateCount + 1 WHERE d.idx = :idx")
    void incrementDonationCount(@Param("idx") Long idx);

    @Query("UPDATE DreamBoardEntity d SET d.donateCount = d.donateCount - 1 WHERE d.idx = :idx")
    void decrementDonationCount(@Param("idx") Long idx);
}
