package com.example.kakao.domain.dreamboard.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.kakao.domain.dreamboard.entity.DreamBoardEntity;

@Repository
public interface DreamBoardRepository extends JpaRepository<DreamBoardEntity, Long>, DreamBoardRepositoryCustom {
    
    // 1. 다건 조회
    // queryDSL 사용 

    // 2. 단건 조회

    // 3. 저장

    // 4. 수정

    // 4.1 조회수 증가
    @Modifying
    @Query("UPDATE DreamBoardEntity db SET db.readCount = db.readCount + 1 WHERE db.id = :id")
    void incrementReadCount(@Param("id") Long id);

    // 5. 삭제

    // 6. 내가 쓴 글 보기
}

