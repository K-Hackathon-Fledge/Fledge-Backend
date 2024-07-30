package com.fledge.fledgeserver.canary.service;

import com.fledge.fledgeserver.canary.dto.CanaryGetDeliveryInfoResponse;
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

import java.util.Optional;

import static com.fledge.fledgeserver.exception.ErrorCode.MEMBER_FORBIDDEN;
import static com.fledge.fledgeserver.exception.ErrorCode.MEMBER_NOT_FOUND;


@Service
@RequiredArgsConstructor
public class CanaryProfileService {

    private final CanaryProfileRepository canaryProfileRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public void createCanaryProfile(CanaryProfileRequest request, String currentUserEmail) {
        Member member = authenticateAndAuthorize(currentUserEmail, request.getUserId());

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
    public int getApprovalStatus(Long userId, String currentUserEmail) {
        authenticateAndAuthorize(currentUserEmail, userId);

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
    public CanaryProfileResponse updateCanaryProfile(Long userId, CanaryProfileUpdateRequest request, String currentUserEmail) {
        authenticateAndAuthorize(currentUserEmail, userId);

        CanaryProfile existingProfile = canaryProfileRepository.findByMemberId(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.CANARY_NOT_FOUND));

        existingProfile.update(request);

        canaryProfileRepository.save(existingProfile);

        return new CanaryProfileResponse(existingProfile);
    }


    private Member authenticateAndAuthorize(String currentUserEmail, Long userId) {
        Member member = memberRepository.findByEmailAndActiveTrue(currentUserEmail)
                .orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));

        if (member.getId() != userId) {
            throw new CustomException(MEMBER_FORBIDDEN);
        }

        return member;
    }

    @Transactional(readOnly = true)
    public CanaryGetDeliveryInfoResponse getCanaryDeliveryInfo(Long memberId) {
        CanaryProfile canary = canaryProfileRepository.findByMemberId(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.CANARY_NOT_FOUND));
        return new CanaryGetDeliveryInfoResponse(
                canary.getAddress(),
                canary.getDetailAddress(),
                canary.getZip(),
                canary.getPhone()
        );
    }
}