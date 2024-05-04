package com.example.kakao.domain.dreamboard.entity;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.example.kakao.global.user.entity.UserEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DreamBoardEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name ="user_fk", nullable = false)
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "category_fk", nullable = false)
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

    @CreatedDate
    @Column(name = "create_date")
    private LocalDateTime createDate;

    @LastModifiedDate
    @Column(name = "modified_date")
    private LocalDateTime modifiedDate;


    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true) // likeEntity의 변수명
    private List<DreamBoardLikeEntity> likeEntitys;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DreamBoardCommentEntity> commentEntitys;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DreamBoardDonationEntity> donationEntitys;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DreamBoardFileEntity> fileEntities;

    
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