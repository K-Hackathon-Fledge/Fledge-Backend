package com.fledge.fledgeserver.support.repository;

import com.fledge.fledgeserver.support.entity.Support;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface SupportRepository extends JpaRepository<Support, Long> {
    /**
     *  한방 쿼리: Fetch Join
     */
    @Query("SELECT s FROM Support s " +
            "JOIN FETCH s.member m " +
            "LEFT JOIN FETCH s.images i " +
            "WHERE s.id = :supportId")
    Optional<Support> findSupportByIdWithFetch(@Param("supportId") Long supportId);
}
