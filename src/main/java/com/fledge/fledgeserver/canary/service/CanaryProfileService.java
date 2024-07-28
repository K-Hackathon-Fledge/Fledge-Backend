package com.fledge.fledgeserver.canary.service;

import com.fledge.fledgeserver.canary.dto.CanaryProfileRequest;
import com.fledge.fledgeserver.canary.dto.CanaryProfileResponse;
import com.fledge.fledgeserver.canary.dto.CanaryProfileUpdateRequest;
import com.fledge.fledgeserver.canary.entity.CanaryProfile;
import com.fledge.fledgeserver.canary.repository.CanaryProfileRepository;
import com.fledge.fledgeserver.exception.CustomException;
import com.fledge.fledgeserver.exception.ErrorCode;
import com.fledge.fledgeserver.member.entity.Member;
import com.fledge.fledgeserver.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class CanaryProfileService {

    private final CanaryProfileRepository canaryProfileRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public void createCanaryProfile(CanaryProfileRequest request) {
        Member member = memberRepository.findById(request.getUserId())
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        boolean exists = canaryProfileRepository.existsByMember(member);
        if (exists) {
            throw new CustomException(ErrorCode.DUPLICATE_APPLICATION);
        }

        CanaryProfile canaryProfile = CanaryProfile.builder()
                .member(member)
                .phone(request.getPhone())
                .birth(request.getBirth())
                .gender(request.getGender())
                .address(request.getAddress())
                .detailAddress(request.getDetailAddress())
                .zip(request.getZip())
                .certificateFilePath(request.getCertificateFilePath())
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .approvalStatus(false)
                .build();

        canaryProfileRepository.save(canaryProfile);
    }

    @Transactional(readOnly = true)
    public CanaryProfileResponse getCanaryProfile(Long userId) {
        CanaryProfile canaryProfile = canaryProfileRepository.findByMemberId(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.CANARY_NOT_FOUND));

        return new CanaryProfileResponse(canaryProfile);
    }

    @Transactional
    public void updateCanaryProfile(Long userId, CanaryProfileUpdateRequest request) {
        CanaryProfile existingProfile = canaryProfileRepository.findByMemberId(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.CANARY_NOT_FOUND));

        existingProfile.update(request);

        canaryProfileRepository.save(existingProfile);
    }
}