package com.example.kakao.domain.dreamboard.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.kakao.domain.dreamboard.entity.DreamBoardEntity;

public interface DreamBoardRepository extends JpaRepository<DreamBoardEntity, Long>, JpaSpecificationExecutor<DreamBoardEntity> {
    @Modifying
    @Query("UPDATE DreamBoardEntity db SET db.readCount = db.readCount + 1 WHERE db.id = :id")
    void incrementReadCount(@Param("id") Long id);
}

