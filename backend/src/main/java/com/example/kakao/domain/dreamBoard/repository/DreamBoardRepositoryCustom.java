package com.example.kakao.domain.dreamboard.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.kakao.domain.dreamboard.entity.DreamBoardEntity;


public interface DreamBoardRepositoryCustom {

    Page<DreamBoardEntity> search(Long id, Long categoryId, String search, Pageable pageable);
    
}
