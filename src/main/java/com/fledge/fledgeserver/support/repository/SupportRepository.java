package com.fledge.fledgeserver.support.repository;

import com.fledge.fledgeserver.support.entity.Support;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SupportRepository extends JpaRepository<Support, Long> {
}
