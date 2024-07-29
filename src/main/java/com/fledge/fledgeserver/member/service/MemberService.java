package com.fledge.fledgeserver.member.service;

import com.fledge.fledgeserver.exception.CustomException;
import com.fledge.fledgeserver.member.dto.MemberResponse;
import com.fledge.fledgeserver.member.entity.Member;
import com.fledge.fledgeserver.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.fledge.fledgeserver.exception.ErrorCode.MEMBER_FORBIDDEN;
import static com.fledge.fledgeserver.exception.ErrorCode.MEMBER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public MemberResponse memberInfo(String email) {
        Member member = memberRepository.findByEmailAndActiveTrue(email)
                .orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));
        return new MemberResponse(member);
    }

    @Transactional(readOnly = true)
    public MemberResponse getMemberDetails(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));
        return new MemberResponse(member);
    }

    @Transactional
    public MemberResponse updateNickname(Long memberId, String newNickname, String currentUserEmail) {

        Member member = memberRepository.findByEmailAndActiveTrue(currentUserEmail)
                .orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));

        if (member.getId() != memberId){
            throw new CustomException(MEMBER_FORBIDDEN);
        }

        member.updateNickname(newNickname);
        memberRepository.save(member);
        return new MemberResponse(member);
    }
}
