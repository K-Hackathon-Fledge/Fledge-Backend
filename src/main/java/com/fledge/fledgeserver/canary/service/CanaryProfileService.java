package com.fledge.fledgeserver.canary.service;

import com.fledge.fledgeserver.auth.dto.OAuthUserImpl;
import com.fledge.fledgeserver.canary.dto.CanaryGetDeliveryInfoResponse;
import com.fledge.fledgeserver.canary.dto.CanaryProfileRequest;
import com.fledge.fledgeserver.canary.dto.CanaryProfileResponse;
import com.fledge.fledgeserver.canary.dto.CanaryProfileUpdateRequest;
import com.fledge.fledgeserver.canary.entity.CanaryProfile;
import com.fledge.fledgeserver.canary.repository.CanaryProfileRepository;
import com.fledge.fledgeserver.common.utils.SecurityUtils;
import com.fledge.fledgeserver.exception.CustomException;
import com.fledge.fledgeserver.exception.ErrorCode;
import com.fledge.fledgeserver.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.fledge.fledgeserver.exception.ErrorCode.MEMBER_FORBIDDEN;


@Service
@RequiredArgsConstructor
public class CanaryProfileService {

    private final CanaryProfileRepository canaryProfileRepository;

    @Transactional
    public void createCanaryProfile(CanaryProfileRequest request, OAuthUserImpl oAuth2User) {
        Member member = authenticateAndAuthorize(oAuth2User, request.getUserId());

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
    public int getApprovalStatus(Long userId, OAuthUserImpl oAuth2User) {
        authenticateAndAuthorize(oAuth2User, userId);

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
    public CanaryProfileResponse updateCanaryProfile(Long userId, CanaryProfileUpdateRequest request, OAuthUserImpl oAuth2User) {
        authenticateAndAuthorize(oAuth2User, userId);

        CanaryProfile existingProfile = canaryProfileRepository.findByMemberId(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.CANARY_NOT_FOUND));

        existingProfile.update(request);

        canaryProfileRepository.save(existingProfile);

        return new CanaryProfileResponse(existingProfile);
    }


    private Member authenticateAndAuthorize(OAuthUserImpl oAuth2User, Long userId) {
        Member member = SecurityUtils.getCurrentMember(oAuth2User);

        if (member.getId() != userId) {
            throw new CustomException(MEMBER_FORBIDDEN);
        }

        return member;
    }

    @Transactional(readOnly = true)
    public CanaryGetDeliveryInfoResponse getCanaryDeliveryInfo(Long userId) {
        CanaryProfile canary = canaryProfileRepository.findCanaryProfileByMemberId(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.CANARY_NOT_FOUND));
        return new CanaryGetDeliveryInfoResponse(
                canary.getName(),
                canary.getAddress(),
                canary.getDetailAddress(),
                canary.getZip(),
                canary.getPhone()
        );
    }
}