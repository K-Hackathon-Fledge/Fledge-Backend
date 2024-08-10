package com.fledge.fledgeserver.support.repository;

import com.fledge.fledgeserver.support.entity.QSupportPost;
import com.fledge.fledgeserver.support.entity.SupportCategory;
import com.fledge.fledgeserver.support.entity.SupportPost;
import com.fledge.fledgeserver.support.entity.SupportPostStatus;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import com.querydsl.core.types.dsl.BooleanExpression;

import java.util.List;

public class SupportPostRepositoryImpl extends QuerydslRepositorySupport implements SupportPostRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Autowired
    public SupportPostRepositoryImpl(JPAQueryFactory queryFactory) {
        super(SupportPost.class);
        this.queryFactory = queryFactory;
    }

    @Override
    public Page<SupportPost> findByCategoryAndSearchAndSupportPostStatusWithImages(
            List<SupportCategory> category, String q, String status, Pageable pageable) {

        QSupportPost supportPost = QSupportPost.supportPost;

        // 카운트 쿼리: 중복 없이 실제 SupportPost 엔티티 개수만 카운트
        long total = queryFactory.selectFrom(supportPost)
                .where(
                        categoryIsNullOrInCategory(supportPost, category),
                        titleOrReasonContains(supportPost, q),
                        statusIsNullOrMatches(supportPost, status)
                ).fetchCount();

        List<SupportPost> results = queryFactory.selectFrom(supportPost)
                .leftJoin(supportPost.images).fetchJoin()
                .where(
                        categoryIsNullOrInCategory(supportPost, category),
                        titleOrReasonContains(supportPost, q),
                        statusIsNullOrMatches(supportPost, status)
                )
                .orderBy(supportPost.createdDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(results, pageable, total);
    }

    private BooleanExpression categoryIsNullOrInCategory(QSupportPost supportPost, List<SupportCategory> category) {
        return category == null || category.isEmpty() ? null : supportPost.supportCategory.in(category);
    }

    private BooleanExpression titleOrReasonContains(QSupportPost supportPost, String q) {
        if (q == null || q.isEmpty()) {
            return null;
        }
        return supportPost.title.containsIgnoreCase(q)
                .or(supportPost.reason.containsIgnoreCase(q));
    }

    private BooleanExpression statusIsNullOrMatches(QSupportPost supportPost, String status) {
        if (status == null || status.isEmpty()) {
            return null;
        }
        if ("ing".equals(status)) {
            return supportPost.supportPostStatus.in(SupportPostStatus.PENDING, SupportPostStatus.IN_PROGRESS);
        } else if ("end".equals(status)) {
            return supportPost.supportPostStatus.in(SupportPostStatus.COMPLETED, SupportPostStatus.TERMINATED);
        } else {
            return null;
        }
    }
}
