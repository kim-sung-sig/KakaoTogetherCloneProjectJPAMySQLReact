package com.example.kakao.DTO;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DreamBoardDTO {
    private Long id;

    private Long userFk;
    private String name;
    private String userProfileImg;

    private Long categoryFk;
    private String categoryName;

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
    private LocalDateTime createDate;

    // 파일을 넘겨주기 위한
    private List<String> fileList;
}
