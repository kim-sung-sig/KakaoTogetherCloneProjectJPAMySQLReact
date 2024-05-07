package com.example.kakao.domain.dreamboard.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.kakao.domain.dreamboard.entity.DreamBoardCategoryEntity;


@Repository
public interface DreamBoardCategoryRepository extends JpaRepository<DreamBoardCategoryEntity, Long>{
    // 흠 일단 있어야 되나?
}
