package com.fledge.fledgeserver.canary.repository;

import com.fledge.fledgeserver.canary.entity.CanaryProfile;
import com.fledge.fledgeserver.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CanaryProfileRepository extends JpaRepository<CanaryProfile, Long> {

    Optional<CanaryProfile> findByMemberId(Long memberId);

    Optional<CanaryProfile> findByMemberIdAndApprovalStatusIsTrue(Long memberId);

    boolean existsByMemberAndApprovalStatusIsTrue(Member member);

    boolean existsByMember(Member member);

}
