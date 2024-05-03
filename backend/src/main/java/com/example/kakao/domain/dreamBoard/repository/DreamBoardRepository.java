package com.example.kakao.domain.dreamBoard.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.example.kakao.domain.dreamBoard.entity.DreamBoardEntity;

public interface DreamBoardRepository extends JpaRepository<DreamBoardEntity, Long>, JpaSpecificationExecutor<DreamBoardEntity> {
    /*
    @Query("""
            select dbe
            from DreamBoardEntity dbe
            where dbe.id = :id
            """)
    Optional<DreamBoardEntity> findById(@Param("id") Long id);
     */
}

