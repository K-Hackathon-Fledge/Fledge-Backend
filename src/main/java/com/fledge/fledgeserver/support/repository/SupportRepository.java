package com.fledge.fledgeserver.support.repository;

import com.fledge.fledgeserver.support.entity.Support;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SupportRepository extends JpaRepository<Support, Long> {
    Optional<Support> findSupportById(Long supportId);

}
