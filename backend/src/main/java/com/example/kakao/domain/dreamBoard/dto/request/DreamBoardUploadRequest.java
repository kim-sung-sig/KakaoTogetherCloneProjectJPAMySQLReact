package com.example.kakao.domain.dreamboard.dto.request;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DreamBoardUploadRequest {
    
    @NotNull(message = "Category number cannot be null")
    private Long categoryNum;
    
    @NotBlank(message = "Title cannot be blank")
    @Size(max = 100, message = "Title cannot be longer than 100 characters")
    private String title;

    @NotBlank(message = "Content cannot be blank")
    @Size(max = 1000, message = "Content cannot be longer than 1000 characters")
    private String content;

    @Size(max = 50, message = "Tag1 cannot be longer than 50 characters")
    private String tag1;

    @Size(max = 50, message = "Tag2 cannot be longer than 50 characters")
    private String tag2;

    @Size(max = 50, message = "Tag3 cannot be longer than 50 characters")
    private String tag3;

    @NotNull(message = "Target price cannot be null")
    @Min(value = 0, message = "Target price must be positive")
    private Integer targetPrice;

    @NotNull(message = "Start date cannot be null")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH")
    private LocalDateTime startDate; // 문자로 받고 LDT로 바꾸자

    @NotNull(message = "End date cannot be null")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH")
    private LocalDateTime endDate; // 문자로 받고 LDT로 바꾸자
    
    @NotEmpty(message = "File list cannot be empty")
    private List<MultipartFile> file;

}
