package com.fledge.fledgeserver.support.repository;

import com.fledge.fledgeserver.exception.CustomException;
import com.fledge.fledgeserver.exception.ErrorCode;
import com.fledge.fledgeserver.support.entity.SupportPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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
}
