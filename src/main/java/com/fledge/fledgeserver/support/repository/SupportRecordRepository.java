package com.fledge.fledgeserver.support.repository;

import com.fledge.fledgeserver.support.entity.SupportPost;
import com.fledge.fledgeserver.support.entity.SupportRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SupportRecordRepository extends JpaRepository<SupportRecord, Long> {
    List<SupportRecord> findAllBySupportPost(SupportPost supportPost);
}
