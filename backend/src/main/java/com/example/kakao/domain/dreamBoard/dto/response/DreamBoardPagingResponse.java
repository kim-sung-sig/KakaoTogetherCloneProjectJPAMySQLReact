package com.example.kakao.domain.dreamboard.dto.response;

import com.example.kakao.global.dto.response.MsgResponse;
import com.example.kakao.global.dto.response.PagingResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DreamBoardPagingResponse{

    private MsgResponse msg;

    private PagingResponse<DreamBoardResponse> data;

}
