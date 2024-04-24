package com.example.kakao.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "kakao_board")
@Data
public class KakaoBoardEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idx;
    private int userFk;
    private int categoryFk;
    private String thumbnail;
    private String tag1;
    private String tag2;
    private String tag3;
    private String title;
    private String content;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private int currentPrice;
    private int targetPrice;
    private int readCount;
    private int likeCount;
    private int commentCount;
    private int donateCount;
    private String ip;
    private LocalDateTime createDate;
}
