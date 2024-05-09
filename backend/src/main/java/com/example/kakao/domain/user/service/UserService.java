package com.example.kakao.domain.user.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.kakao.domain.user.entity.UserEntity;
import com.example.kakao.domain.user.repository.UserRepository;

@Service("userService")
public class UserService {
    
    @Autowired
    private UserRepository userRepository;

    public Optional<UserEntity> findById(Long id){
        return userRepository.findById(id);
    }

}
