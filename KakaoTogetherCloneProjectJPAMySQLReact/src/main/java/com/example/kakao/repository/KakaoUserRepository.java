package com.example.kakao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.kakao.entity.KakaoUserEntity;

@Repository
public interface KakaoUserRepository extends JpaRepository<KakaoUserEntity, Integer>{

}
