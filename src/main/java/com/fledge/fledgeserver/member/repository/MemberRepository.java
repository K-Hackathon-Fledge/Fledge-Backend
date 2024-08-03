package com.fledge.fledgeserver.member.repository;

import com.fledge.fledgeserver.exception.CustomException;
import com.fledge.fledgeserver.exception.ErrorCode;
import com.fledge.fledgeserver.member.entity.Member;
import com.fledge.fledgeserver.response.ErrorStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findBySocialId(Long socialId);
    Optional<Member> findByEmailAndActiveTrue(String email);

    Optional<Member> findMemberById(Long memberId);

    default Member findMemberByIdOrThrow(Long memberId) {
        return findMemberById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
    }
}

