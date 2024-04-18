package com.example.kakao.DTO;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table
public class MyUserDTO {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int idx;
	private String username;
	private String password;
	private String role;
	private String type;
	
	private String nickName;
	
	private LocalDateTime signUpdate;
	private LocalDateTime lastLoginDate;
	
	private String profile;
}
