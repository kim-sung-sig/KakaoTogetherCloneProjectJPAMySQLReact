package com.example.kakao.domain.dreamBoard.entity;

import java.time.LocalDateTime;
import java.util.List;

import com.example.kakao.global.jpa.BaseEntity;
import com.example.kakao.global.user.entity.UserEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Getter @Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DreamBoardEntity extends BaseEntity {

    @JoinColumn(name ="user_fk", nullable = false)
    @ManyToOne
    private UserEntity user;

    @JoinColumn(name = "category_fk", nullable = false)
    @ManyToOne
    private DreamBoardCategoryEntity category;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "thumbnail")
    private String thumbnail;

    @Column(name = "tag1")
    private String tag1;

    @Column(name = "tag2")
    private String tag2;

    @Column(name = "tag3")
    private String tag3;

    @Column(name = "current_price", nullable = false)
    private int currentPrice;

    @Column(name = "target_price", nullable = false)
    private int targetPrice;

    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDateTime endDate;

    @Column(name = "ip", nullable = false)
    private String ip;




    
    @OneToMany(mappedBy = "board") // likeEntity의 변수명
    private List<DreamBoardLikeEntity> likeEntitys;

    @OneToMany(mappedBy = "board")
    private List<DreamBoardCommentEntity> commentEntitys;

    @OneToMany(mappedBy = "board")
    private List<DreamBoardDonationEntity> donationEntitys;

    public int getLikeCount(){
        return likeEntitys != null ? likeEntitys.size() : 0;
    }

    public int getCommentCount(){
        return commentEntitys != null ? commentEntitys.size() : 0;
    }

    public int getDonationCount(){
        return donationEntitys != null ? donationEntitys.size() : 0;
    }
}
