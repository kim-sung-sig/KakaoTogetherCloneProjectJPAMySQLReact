package com.example.kakao.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
public class DreamBoarcCategoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idx", updatable = false)
    private Long idx;

    @Column(name = "category_name", nullable = false)
    private String categoryName;
}
