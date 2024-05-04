package com.example.kakao.global.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long id;
    private String username;
    private String name;
    private String role = "ROLE_USER";

    @Builder
    public UserDTO(String username, String name, String role){
        this.username = username;
        this.name = name;
        this.role = role;
    }
}
