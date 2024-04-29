package com.example.kakao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.kakao.entity.DreamBoardEntity;

@Repository
public interface DreamBoardRepository extends JpaRepository<DreamBoardEntity, Long> {

}
