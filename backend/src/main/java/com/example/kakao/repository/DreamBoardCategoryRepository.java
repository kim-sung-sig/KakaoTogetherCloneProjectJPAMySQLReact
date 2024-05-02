package com.example.kakao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.kakao.entity.DreamBoardCategoryEntity;


@Repository
public interface DreamBoardCategoryRepository extends JpaRepository<DreamBoardCategoryEntity, Long>{
    
}
