package com.example.kakao.domain.dreamboard.dto.request;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DreamBoardUploadRequest {
    
    private Long categoryNum;
    private String title;
    private String content;
    private String tag1;
    private String tag2;
    private String tag3;
    private Integer targetPrice;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH")
    private LocalDateTime startDate; // 문자로 받고 LDT로 바꾸자
    @DateTimeFormat(pattern = "yyyy-MM-dd HH")
    private LocalDateTime endDate; // 문자로 받고 LDT로 바꾸자

    private List<MultipartFile> file;

    private String ip;

}
