package com.example.kakao.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "kakao_board_comment")
@Data
public class BoardCommentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idx;
    private int userFk;
    private int boardFk;
    private String content;
    private int likeCount;
    private String ip;
    private LocalDateTime createDate;
}
