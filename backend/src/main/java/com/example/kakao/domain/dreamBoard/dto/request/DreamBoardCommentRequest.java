package com.example.kakao.domain.dreamboard.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DreamBoardCommentRequest {

    @NotBlank @Size(max = 100, message = "comment cannot be longer than 100 characters")
    private String comment;

}
