package com.fledge.fledgeserver.support.repository;

import com.fledge.fledgeserver.exception.CustomException;
import com.fledge.fledgeserver.exception.ErrorCode;
import com.fledge.fledgeserver.support.entity.SupportCategory;
import com.fledge.fledgeserver.support.entity.SupportPost;
import com.fledge.fledgeserver.support.entity.SupportPostStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SupportPostRepository extends JpaRepository<SupportPost, Long> {
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
            "LEFT JOIN FETCH sp.images " +
            "WHERE (:category IS NULL OR sp.supportCategory IN :category) " +
            "AND (sp.title LIKE %:q% OR sp.reason LIKE %:q%) " +
            "AND (:status IS NULL OR (" +
            "    (:status = 'ing' AND sp.supportPostStatus IN " +
            "        (com.fledge.fledgeserver.support.entity.SupportPostStatus.PENDING, " +
            "         com.fledge.fledgeserver.support.entity.SupportPostStatus.IN_PROGRESS)) " +
            "    OR " +
            "    (:status = 'end' AND sp.supportPostStatus IN " +
            "        (com.fledge.fledgeserver.support.entity.SupportPostStatus.COMPLETED, " +
            "         com.fledge.fledgeserver.support.entity.SupportPostStatus.TERMINATED))" +
            ")) " +
            "ORDER BY sp.createdDate DESC")
    Page<SupportPost> findByCategoryAndSearchAndSupportPostStatusWithImages(@Param("category") List<SupportCategory> category,
                                                                            @Param("q") String q,
                                                                            @Param("status") String status,
                                                                            Pageable pageable);

    @Query("SELECT sp FROM SupportPost sp " +
            "WHERE FUNCTION('DATEDIFF', sp.expirationDate, CURRENT_DATE) <= 7 " +
            "AND (sp.supportPostStatus = :pending OR sp.supportPostStatus = :inProgress) " +
            "ORDER BY sp.expirationDate ASC")
    List<SupportPost> findByExpirationDateWithinSevenDays(@Param("pending") SupportPostStatus pending, @Param("inProgress") SupportPostStatus inProgress);

    @Query("SELECT sp FROM SupportPost sp WHERE sp.supportPostStatus = com.fledge.fledgeserver.support.entity.SupportPostStatus.PENDING " +
            "OR sp.supportPostStatus = com.fledge.fledgeserver.support.entity.SupportPostStatus.IN_PROGRESS")
    List<SupportPost> findAllBySupportPostStatusOr();

}
