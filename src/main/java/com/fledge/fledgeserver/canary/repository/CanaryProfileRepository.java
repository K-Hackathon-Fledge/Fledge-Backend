package com.fledge.fledgeserver.canary.repository;

import com.fledge.fledgeserver.canary.entity.CanaryProfile;
import com.fledge.fledgeserver.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CanaryProfileRepository extends JpaRepository<CanaryProfile, Long> {
    boolean existsByMember(Member member);
}
