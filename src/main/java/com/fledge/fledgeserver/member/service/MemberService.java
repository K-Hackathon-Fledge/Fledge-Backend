package com.fledge.fledgeserver.member.service;

import com.fledge.fledgeserver.auth.dto.OAuthUserImpl;
import com.fledge.fledgeserver.common.utils.SecurityUtils;
import com.fledge.fledgeserver.member.dto.MemberResponse;
import com.fledge.fledgeserver.member.entity.Member;
import com.fledge.fledgeserver.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public MemberResponse memberInfo(OAuthUserImpl oAuth2User) {
        Member member = SecurityUtils.getCurrentMember(oAuth2User);

        return new MemberResponse(member);
    }

    @Transactional(readOnly = true)
    public MemberResponse getMemberDetails(Long memberId) {
        Member member = SecurityUtils.checkAndGetCurrentUser(memberId);

        return new MemberResponse(member);
    }

    @Transactional
    public MemberResponse updateNickname(Long memberId, String newNickname) {
        Member member = SecurityUtils.checkAndGetCurrentUser(memberId);

        member.updateNickname(newNickname);
        memberRepository.save(member);
        return new MemberResponse(member);
    }
}
