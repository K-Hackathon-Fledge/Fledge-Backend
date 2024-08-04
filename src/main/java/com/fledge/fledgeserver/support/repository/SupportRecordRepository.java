package com.fledge.fledgeserver.support.repository;

import com.fledge.fledgeserver.support.entity.SupportPost;
import com.fledge.fledgeserver.support.entity.SupportRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SupportRecordRepository extends JpaRepository<SupportRecord, Long> {
    List<SupportRecord> findAllBySupportPost(SupportPost supportPost);

    @Query("SELECT SUM(sr.amount) FROM SupportRecord sr WHERE sr.supportPost.id = :supportPostId")
    int sumSupportedPriceBySupportPostId(@Param("supportPostId") Long supportPostId);
}
