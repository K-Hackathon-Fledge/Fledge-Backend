package com.fledge.fledgeserver.member.repository;

import com.fledge.fledgeserver.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findBySocialId(Long socialId);
    Optional<Member> findByEmailAndActiveTrue(String email);
}

