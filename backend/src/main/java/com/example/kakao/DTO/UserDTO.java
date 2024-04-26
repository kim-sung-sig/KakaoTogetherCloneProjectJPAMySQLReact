package com.example.kakao.DTO;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserDTO {
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
