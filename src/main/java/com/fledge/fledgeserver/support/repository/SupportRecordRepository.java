package com.fledge.fledgeserver.support.repository;

import com.fledge.fledgeserver.support.entity.SupportRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SupportRecordRepository extends JpaRepository<SupportRecord, Long> {
}
