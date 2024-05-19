package com.example.kakao.domain.dreamboard.dto.request;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DreamBoardUpdateRequest {

    private Long id;
    private Long categoryNum;
    private String title;
    private String content;
    private String tag1;
    private String tag2;
    private String tag3;
    private Integer targetPrice;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH")
    private LocalDateTime endDate; // 문자로 받고 LDT로 바꾸자
    
    private List<Long> existingfile; // 기존 사진의 ID 목록
    private List<MultipartFile> file; // 새로 추가되는 사진 파일 목록
    private Map<Long, Integer> fileOrder; // 사진 ID와 그 순서를 나타내는 맵

    private String ip;
}
