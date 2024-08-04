package com.fledge.fledgeserver.support.repository;

import com.fledge.fledgeserver.exception.CustomException;
import com.fledge.fledgeserver.exception.ErrorCode;
import com.fledge.fledgeserver.support.entity.SupportCategory;
import com.fledge.fledgeserver.support.entity.SupportPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SupportRepository extends JpaRepository<SupportPost, Long> {
    /**
     *  한방 쿼리: Fetch Join
     */
    @Query("SELECT s FROM SupportPost s " +
            "JOIN FETCH s.member m " +
            "LEFT JOIN FETCH s.images i " +
            "WHERE s.id = :supportId")
    Optional<SupportPost> findSupportByIdWithFetch(@Param("supportId") Long supportId);

    Optional<SupportPost> findSupportById(Long supportId);

    default SupportPost findSupportByIdOrThrow(Long supportId) {
        return findSupportById(supportId)
                .orElseThrow(() -> new CustomException(ErrorCode.SUPPORT_NOT_FOUND));
    }

    @Query("SELECT sp FROM SupportPost sp " +
            "WHERE (:category IS NULL OR sp.supportCategory IN :category) " + // 카테고리 필터링
            "AND (sp.title LIKE %:q% OR sp.reason LIKE %:q%)") // 제목 및 이유 필터링
    Page<SupportPost> findByCategoryAndSearch(@Param("category") List<SupportCategory> category,
                                              @Param("q") String q,
                                              Pageable pageable);


}
