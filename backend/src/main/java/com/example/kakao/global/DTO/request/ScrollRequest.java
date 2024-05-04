package com.example.kakao.global.DTO.request;

import lombok.Data;

@Data
public class ScrollRequest {
    Long lastItemIdx;
    Integer size;
    Long categoryNum;
    String search;
}
