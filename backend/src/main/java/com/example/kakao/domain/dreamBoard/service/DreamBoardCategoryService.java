package com.example.kakao.domain.dreamboard.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.kakao.domain.dreamboard.entity.DreamBoardCategoryEntity;
import com.example.kakao.domain.dreamboard.repository.DreamBoardCategoryRepository;

@Service
public class DreamBoardCategoryService {

    @Autowired
    private DreamBoardCategoryRepository categoryRepository;

    public Optional<DreamBoardCategoryEntity> findById(Long id){
        return categoryRepository.findById(id);
    }

    public List<DreamBoardCategoryEntity> findAll(){
        return categoryRepository.findAll();
    }
    
}
