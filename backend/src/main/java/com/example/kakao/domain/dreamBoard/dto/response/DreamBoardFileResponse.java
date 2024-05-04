package com.example.kakao.domain.dreamboard.dto.response;

import java.util.List;
import java.util.stream.Collectors;

import com.example.kakao.domain.dreamboard.entity.DreamBoardFileEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DreamBoardFileResponse {
    private Long id;
    private String saveFileName;

    public static List<DreamBoardFileResponse> convertToFileResponses(List<DreamBoardFileEntity> fileEntities) {
        if(fileEntities == null || fileEntities.size() == 0){
            return null;
        }
        return fileEntities.stream()
                .map(entity -> new DreamBoardFileResponse(entity.getId(), entity.getSaveFileName()))
                .collect(Collectors.toList());
    }
}
