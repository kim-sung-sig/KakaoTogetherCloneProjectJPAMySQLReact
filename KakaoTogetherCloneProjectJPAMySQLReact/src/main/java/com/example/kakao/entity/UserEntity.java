package com.example.kakao.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "my_user")
@Data
@NoArgsConstructor
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    private String username;

    private String name;

    private String email;

    private String role = "ROLE_USER";

    private String type;

    private LocalDateTime createDate;

    @Builder
    public UserEntity(String username, String name, String email, String role, String type){
        this.username = username;
        this.name = name;
        this.email = email;
        this.role = role;
        this.type = type;
    }
}
