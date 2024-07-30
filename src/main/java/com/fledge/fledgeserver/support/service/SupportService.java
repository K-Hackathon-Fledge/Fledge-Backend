package com.fledge.fledgeserver.support.service;

import com.fledge.fledgeserver.canary.repository.CanaryProfileRepository;
import com.fledge.fledgeserver.exception.CustomException;
import com.fledge.fledgeserver.exception.ErrorCode;
import com.fledge.fledgeserver.file.FileService;
import com.fledge.fledgeserver.support.dto.request.SupportCreateRequestDto;
import com.fledge.fledgeserver.support.dto.response.SupportDetailGetResponseDto;
import com.fledge.fledgeserver.support.entity.Support;
import com.fledge.fledgeserver.support.entity.SupportImage;
import com.fledge.fledgeserver.support.repository.SupportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SupportService {
    private final CanaryProfileRepository canaryProfileRepository;
    private final SupportRepository supportRepository;
    private final FileService fileService;

    @Transactional
    public void createSupport(Long memberId, SupportCreateRequestDto supportCreateRequestDto) {
         // 자립 청소년인지 검증 -> canary_profile 테이블에 없으면 권한이 없는 것
        canaryProfileRepository.findCanaryProfileByMemberId(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.UNAUTHORIZED_MEMBER));


        Support support = Support.builder()
                .title(supportCreateRequestDto.getTitle())
                .reason(supportCreateRequestDto.getReason())
                .item((supportCreateRequestDto.getItem()))
                .price(supportCreateRequestDto.getPrice())
                .purchaseUrl(supportCreateRequestDto.getPurchaseUrl())
                .checkPeriod(supportCreateRequestDto.getCheckPeriod())
                .checkCount(supportCreateRequestDto.getCheckCount())
                .expirationDate(supportCreateRequestDto.getExpirationDate())
                .address(supportCreateRequestDto.getAddress())
                .detailAddress(supportCreateRequestDto.getDetailAddress())
                .zip(supportCreateRequestDto.getZip())
                .name(supportCreateRequestDto.getName())
                .phone(supportCreateRequestDto.getPhone())
                .build();

        supportRepository.save(support);

        for (String imageUrl : supportCreateRequestDto.getImages()) {
            SupportImage supportImage = SupportImage.builder()
                    .support(support)
                    .imageUrl(imageUrl)
                    .build();
            support.getImages().add(supportImage);
        }
    }

    @Transactional(readOnly = true)
    public SupportDetailGetResponseDto getSupport(Long supportId) {
        // 후원 게시글 조회
        Support support = supportRepository.findSupportById(supportId)
                .orElseThrow(() -> new CustomException(ErrorCode.SUPPORT_NOT_FOUND));

        // 후원 게시글의 이미지 URL -> Presigned URL로 변환
        List<String> presignedImageUrl = support.getImages().stream()
                .map(SupportImage::getImageUrl)
                .map(fileService::getFileUrl)
                .collect(Collectors.toList());

        // TODO :: 후원내역(SupportRecord)에 대한 내용 함께 반환

        return new SupportDetailGetResponseDto(support, presignedImageUrl);
    }
}
