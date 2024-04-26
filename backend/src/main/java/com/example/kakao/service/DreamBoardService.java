package com.example.kakao.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.kakao.DTO.DreamBoardDTO;
import com.example.kakao.entity.DreamBoardEntity;
import com.example.kakao.repository.DreamBoardRepository;

@Service
public class DreamBoardService {
    @Autowired
    private DreamBoardRepository boardRepository;

    // 목록 페이징 얻기
    public List<DreamBoardDTO> getPagingList(Long lastItemIdx, int sizeOfPage){
        Page<DreamBoardEntity> page;
        Pageable pageable = PageRequest.of(0, sizeOfPage);
        if(lastItemIdx == null){
            page = boardRepository.findAll(pageable);
        } else {
            page = boardRepository.findByIdxLessThanOrderByIdxDesc(lastItemIdx, pageable);
        }
        List<DreamBoardDTO> list = new ArrayList<>();

        for(var entity : page.getContent()){
            var boardDTO = new DreamBoardDTO();
            BeanUtils.copyProperties(entity, boardDTO);
            list.add(boardDTO);
        }
        return list;
    }
}
