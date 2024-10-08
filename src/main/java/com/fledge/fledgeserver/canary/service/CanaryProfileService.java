package com.fledge.fledgeserver.canary.service;

import com.fledge.fledgeserver.canary.dto.*;
import com.fledge.fledgeserver.canary.entity.CanaryProfile;
import com.fledge.fledgeserver.canary.repository.CanaryProfileRepository;
import com.fledge.fledgeserver.common.utils.SecurityUtils;
import com.fledge.fledgeserver.exception.CustomException;
import com.fledge.fledgeserver.exception.ErrorCode;
import com.fledge.fledgeserver.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class CanaryProfileService {

    private final CanaryProfileRepository canaryProfileRepository;

    @Transactional
    public void createCanaryProfile(CanaryProfileRequest request) {
        Member member = SecurityUtils.checkAndGetCurrentUser(request.getUserId());

        if (canaryProfileRepository.existsByMember(member)){
            throw new CustomException(ErrorCode.DUPLICATE_APPLICATION);
        }

        CanaryProfile canaryProfile = CanaryProfile.builder()
                .member(member)
                .name(request.getName())
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
    public int getApprovalStatus(Long userId) {
        SecurityUtils.checkAndGetCurrentUser(userId);

        CanaryProfile canaryProfile = canaryProfileRepository.findByMemberId(userId)
                .orElse(null);

        if (canaryProfile == null) {
            return 0;
        } else if (!canaryProfile.getApprovalStatus()) {
            return 1;
        } else {
            return 2;
        }
    }

    @Transactional(readOnly = true)
    public CanaryProfileResponse getCanaryProfile(Long userId) {
        CanaryProfile canaryProfile = canaryProfileRepository.findByMemberId(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.CANARY_NOT_FOUND));

        return new CanaryProfileResponse(canaryProfile);
    }

    @Transactional
    public CanaryProfileResponse updateCanaryProfile(Long userId, CanaryProfileUpdateRequest request) {
        Member member = SecurityUtils.checkAndGetCurrentUser(userId);

        if (!canaryProfileRepository.existsByMemberAndApprovalStatusIsTrue(member)){
            throw new CustomException(ErrorCode.CANARY_NOT_FOUND, "인증되지 않은 자립준비청년 입니다.");
        }

        CanaryProfile existingProfile = canaryProfileRepository.findByMemberId(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.CANARY_NOT_FOUND));

        existingProfile.update(request);

        canaryProfileRepository.save(existingProfile);

        return new CanaryProfileResponse(existingProfile);
    }


    @Transactional(readOnly = true)
    public CanaryGetDeliveryInfoResponse getCanaryDeliveryInfo() {
        Long userId = SecurityUtils.getCurrentUserId();
        CanaryProfile canary = canaryProfileRepository.findByMemberIdAndApprovalStatusIsTrue(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.CANARY_NOT_FOUND));
        return new CanaryGetDeliveryInfoResponse(
                canary.getName(),
                canary.getAddress(),
                canary.getDetailAddress(),
                canary.getZip(),
                canary.getPhone()
        );
    }

    @Transactional(readOnly = true)
    public CanaryProfileGetResponse getCanaryForSupport(Long memberId) {
        CanaryProfile canaryProfile = canaryProfileRepository.findByMemberIdAndApprovalStatusIsTrue(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.CANARY_NOT_FOUND));

        return new CanaryProfileGetResponse(canaryProfile);
    }
}