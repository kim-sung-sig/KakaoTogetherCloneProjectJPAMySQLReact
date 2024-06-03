package com.example.kakao.domain.dreamboard.entity;

import java.time.LocalDateTime;
import java.util.Set;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.example.kakao.domain.user.entity.UserEntity;

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
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Data
@Builder
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

    @Column(name = "read_count")
    private int readCount;

    @Column(name = "is_used", columnDefinition = "TINYINT(1) DEFAULT 1")
    private Integer isUsed;


    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true) // likeEntity의 변수명
    private Set<DreamBoardLikeEntity> likeEntitys;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<DreamBoardCommentEntity> commentEntitys;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<DreamBoardDonationEntity> donationEntitys;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<DreamBoardFileEntity> fileEntities;

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
