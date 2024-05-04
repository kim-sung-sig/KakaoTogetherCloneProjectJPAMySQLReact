package com.example.kakao.domain.dreamboard.entity;

import java.util.Date;

import com.example.kakao.global.user.entity.UserEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class DreamBoardCommentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_fk", nullable = false)
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "board_fk", nullable = false)
    private DreamBoardEntity board;
    
    @Column(name = "comment", nullable = false)
    private String comment;

    @Column(name = "create_date")
    private Date createDate;

    @Column(name = "ip")
    private String ip;
    
}
