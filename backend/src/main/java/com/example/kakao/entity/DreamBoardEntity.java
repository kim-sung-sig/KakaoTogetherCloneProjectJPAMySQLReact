package com.example.kakao.entity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter @ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class DreamBoardEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @JoinColumn(name ="user_fk", nullable = false)
    @ManyToOne
    private UserEntity user;

    @JoinColumn(name = "category_fk", nullable = false)
    @ManyToOne
    private DreamBoarcCategoryEntity category;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "thumbnail")
    private String thumbnail;

    @Column(name = "current_price", nullable = false)
    private int currentPrice;

    @Column(name = "target_price", nullable = false)
    private int targetPrice;

    @Column(name = "start_date", nullable = false)
    private Date startDate;

    @Column(name = "end_date", nullable = false)
    private Date endDate;

    @Column(name = "create_date", nullable = false)
    private Date createDate;

    @Column(name = "ip", nullable = false)
    private String ip;
}
