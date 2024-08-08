package com.fledge.fledgeserver.support.repository;

import com.fledge.fledgeserver.support.entity.SupportPost;
import com.fledge.fledgeserver.support.entity.SupportRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface SupportRecordRepository extends JpaRepository<SupportRecord, Long> {
    List<SupportRecord> findAllBySupportPost(SupportPost supportPost);

    @Query("SELECT COALESCE(SUM(sr.amount), 0) FROM SupportRecord sr WHERE sr.supportPost.id = :supportPostId")
    int sumSupportedPriceBySupportPostId(@Param("supportPostId") Long supportPostId);

    // Soft Delete 시 한방 쿼리 용
    @Modifying
    @Query("UPDATE SupportRecord sr SET sr.deletedAt = :deletedAt WHERE sr.id IN :ids")
    void softDeleteByIds(@Param("ids") List<Long> ids, @Param("deletedAt") LocalDateTime deletedAt);
}
