package com.example.kakao.domain.dreamboard.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import com.example.kakao.domain.dreamboard.entity.DreamBoardEntity;

import lombok.Data;

@Data
public class DreamBoardResponse {

    public DreamBoardResponse(DreamBoardEntity e){
        this.id = e.getId();
        this.userFk = e.getUser().getId();
        this.name = e.getUser().getName();
        this.userProfileImg = e.getUser().getProfileImg();
        this.categoryFk = e.getCategory().getId();
        this.categoryName = e.getCategory().getCategoryName();
        this.title = e.getTitle();
        this.content = e.getContent();
        this.tag1 = e.getTag1();
        this.tag2 = e.getTag2();
        this.tag3 = e.getTag3();
        this.currentPrice = e.getCurrentPrice();
        this.targetPrice = e.getTargetPrice();
        this.startDate = e.getStartDate();
        this.endDate = e.getEndDate();
        this.createDate = e.getCreateDate();
        this.likeCount = e.getLikeCount();
        this.commentCount = e.getCommentCount();
        this.donationCount = e.getDonationCount();
        this.fileList = DreamBoardFileResponse.convertToFileResponses(e.getFileEntities());
    }


    private Long id;

    // 유저 관련
    private Long userFk;
    private String name;
    private String userProfileImg;

    // 카테고리 관련
    private Long categoryFk;
    private String categoryName;

    private String title;
    private String content;
    private String tag1;
    private String tag2;
    private String tag3;
    private int currentPrice;
    private int targetPrice;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private LocalDateTime createDate;

    // 좋아요 관련
    private int likeCount;
    private boolean isLike = false;

    // 댓글 관련
    private int commentCount;

    // 후원 관련
    private int donationCount;

    // 파일
    private List<DreamBoardFileResponse> fileList;

    private boolean isOwner = false;
}
