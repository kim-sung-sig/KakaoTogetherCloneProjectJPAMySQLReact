package com.example.kakao.domain.dreamboard.dto.response;

import java.time.LocalDateTime;

import com.example.kakao.domain.dreamboard.entity.DreamBoardCommentEntity;

import lombok.Data;

@Data
public class DreamBoardCommentResponse {

    public DreamBoardCommentResponse(DreamBoardCommentEntity e){
        this.id = e.getId();
        this.userNum = e.getUser().getId();
        this.name = e.getUser().getName();
        this.userProfileImg = e.getUser().getProfileImg();

        this.comment = e.getComment();
        this.createDate = e.getCreateDate();
    }

    private Long id;                    // 기본키

    // 유저 관련
    private Long userNum;               // 유저번호
    private String name;                // 유저이름
    private String userProfileImg;      // 유저프로필사진

    private Long boardNum;              // 게시글번호

    private String comment;             // 댓글내용
    private LocalDateTime createDate;   // 게시일

    private boolean isLike = false;     // 댓글공감 했는지 // 확실히 이런건 세션방식이 편하긴하네..
    private boolean isOwner = false;    // 댓글 주인인지

}
