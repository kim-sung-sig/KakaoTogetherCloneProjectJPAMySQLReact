package com.example.kakao.domain.dreamboard.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.kakao.domain.dreamboard.entity.DreamBoardFileEntity;

@Repository
public interface DreamBoardFileRepository extends JpaRepository<DreamBoardFileEntity, Long> {

    List<DreamBoardFileEntity> findByBoardId(Long id);
    
    // board에 해당하는 것들의 id들 얻기
    @Query("select dbfe.id from DreamBoardFileEntity dbfe where dbfe.board.id =: boardId")
    List<Long> findIdByBoardId(Long boardId);
    // 2. 사진 삭제하기


}
