package com.example.kakao.domain.dreamboard.dto.response;

import java.util.Collection;
import java.util.Comparator;
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
    private Integer orders;

    public static List<DreamBoardFileResponse> convertToFileResponses(Collection<DreamBoardFileEntity> fileEntities) {
        if(fileEntities == null || fileEntities.size() == 0){
            return null;
        }
        return fileEntities.stream()
                .sorted(Comparator.comparingInt(DreamBoardFileEntity::getOrders))
                .map(entity -> new DreamBoardFileResponse(entity.getId(), entity.getSaveFileName(), entity.getOrders()))
                .collect(Collectors.toList());
    }
}
