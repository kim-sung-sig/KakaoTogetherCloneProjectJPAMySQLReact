package com.example.kakao.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Data;

@Entity
@Table(name = "kakao_user")
@Data
public class KakaoUserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idx;
    private String username;
    private String password;
    private String role;
    private String type;
    private String name;
    private int nameNum;
    private String email;
    private String profileImg;
    private LocalDateTime createDate;
    private LocalDateTime lastLoginDate;


    @Builder
    public KakaoUserEntity(String username, String password, String type, String name, int nameNum, String email){
        this.username = username;
        this.password = password;
        this.type = type;
        this.name = name;
        this.nameNum = nameNum;
        this.email = email;
        this.role = "ROLE_USER";
    }
}
