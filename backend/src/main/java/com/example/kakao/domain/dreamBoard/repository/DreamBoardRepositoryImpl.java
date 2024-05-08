package com.example.kakao.domain.dreamboard.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import com.example.kakao.domain.dreamboard.entity.DreamBoardEntity;
import com.example.kakao.domain.dreamboard.entity.QDreamBoardEntity;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

public class DreamBoardRepositoryImpl implements DreamBoardRepositoryCustom {

    @Autowired
    private JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<DreamBoardEntity> search(Long lastItemid, Long categoryId, String search, Pageable pageable) {
        QDreamBoardEntity dreamBoardEntity = QDreamBoardEntity.dreamBoardEntity;

        JPAQuery<DreamBoardEntity> jpaQuery = jpaQueryFactory
            .select(dreamBoardEntity)
            .from(dreamBoardEntity)
            .where(
                lastItemid != null ? dreamBoardEntity.id.lt(lastItemid) : null,
                search != null && !search.isEmpty() ?
                    dreamBoardEntity.title.contains(search)
                    .or(dreamBoardEntity.tag1.contains(search))
                    .or(dreamBoardEntity.tag2.contains(search))
                    .or(dreamBoardEntity.tag3.contains(search)) : null,
                categoryId != null ? dreamBoardEntity.category.id.eq(categoryId) : null
            )
            .orderBy(dreamBoardEntity.id.desc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize());

        List<DreamBoardEntity> resultList = jpaQuery.fetch();

        JPAQuery<Long> jpaQueryCount = jpaQueryFactory
            .select(dreamBoardEntity.count())
            .from(dreamBoardEntity)
            .where(
                lastItemid != null ? dreamBoardEntity.id.lt(lastItemid) : null,
                search != null && !search.isEmpty() ?
                    dreamBoardEntity.title.contains(search)
                    .or(dreamBoardEntity.tag1.contains(search))
                    .or(dreamBoardEntity.tag2.contains(search))
                    .or(dreamBoardEntity.tag3.contains(search)) : null,
                categoryId != null ? dreamBoardEntity.category.id.eq(categoryId) : null
            );

        return PageableExecutionUtils.getPage(resultList, pageable, jpaQueryCount::fetchOne);
    }

}
