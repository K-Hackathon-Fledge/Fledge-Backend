package com.fledge.fledgeserver.support.service;

import com.fledge.fledgeserver.canary.repository.CanaryProfileRepository;
import com.fledge.fledgeserver.exception.CustomException;
import com.fledge.fledgeserver.exception.ErrorCode;
import com.fledge.fledgeserver.file.FileService;
import com.fledge.fledgeserver.member.entity.Member;
import com.fledge.fledgeserver.member.entity.Role;
import com.fledge.fledgeserver.member.repository.MemberRepository;
import com.fledge.fledgeserver.support.dto.request.SupportRecordCreateRequestDto;
import com.fledge.fledgeserver.support.dto.request.SupportCreateRequestDto;
import com.fledge.fledgeserver.support.dto.response.SupportGetResponseDto;
import com.fledge.fledgeserver.support.entity.SupportPost;
import com.fledge.fledgeserver.support.entity.SupportImage;
import com.fledge.fledgeserver.support.entity.SupportRecord;
import com.fledge.fledgeserver.support.repository.SupportImageRepository;
import com.fledge.fledgeserver.support.repository.SupportRecordRepository;
import com.fledge.fledgeserver.support.repository.SupportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SupportService {
    private final MemberRepository memberRepository;
    private final SupportRepository supportRepository;
    private final FileService fileService;
    private final SupportRecordRepository supportRecordRepository;

    @Transactional
    public void createSupport(Long memberId, SupportCreateRequestDto supportCreateRequestDto) {
        Member member = memberRepository.findMemberById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        if (member.getRole() != Role.CANARY) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_REQUEST);
        }

        SupportPost supportPost = SupportPost.builder()
                .member(member)
                .supportCreateRequestDto(supportCreateRequestDto)
                .build();
        supportRepository.save(supportPost);

        for (String imageUrl : supportCreateRequestDto.getImages()) {
            SupportImage supportImage = SupportImage.builder()
                    .supportPost(supportPost)
                    .imageUrl(imageUrl)
                    .build();
            supportPost.getImages().add(supportImage);
        }
    }

    @Transactional(readOnly = true)
    public SupportGetResponseDto getSupport(Long supportId) {
        SupportPost supportPost = supportRepository.findSupportByIdWithFetch(supportId)
                .orElseThrow(() -> new CustomException(ErrorCode.SUPPORT_NOT_FOUND));

        return new SupportGetResponseDto(
                supportPost.getMember().getId(),
                supportPost.getMember().getNickname(),
                supportPost.getTitle(),
                supportPost.getReason(),
                supportPost.getItem(),
                supportPost.getPurchaseUrl(),
                supportPost.getPrice(),
                // Images Presigned-URL처리
                supportPost.getImages().stream()
                        .map(supportImage -> fileService.getFileUrl(supportImage.getImageUrl()))
                        .toList(),
                supportPost.getExpirationDate()
        );
    }

    @Transactional
    public void createSupportRecord(Long supportId, SupportRecordCreateRequestDto supportRecordCreateRequestDto, Long memberId) {
        Member member = memberRepository.findMemberByIdOrThrow(memberId); // 후원자
        SupportPost supportPost = supportRepository.findSupportByIdOrThrow(supportId); // 게시글

        // 후원 내역 기록
        SupportRecord supportRecord = SupportRecord.builder()
                .member(member)
                .supportPost(supportPost)
                .bankName(supportRecordCreateRequestDto.getBankName())
                .bankCode(supportRecordCreateRequestDto.getBankCode())
                .account(supportRecordCreateRequestDto.getAccount())
                .amount(supportRecordCreateRequestDto.getAmount())
                .build();
        supportRecordRepository.save(supportRecord);

        // 후원 게시글 상태 변경
        supportPost.support();
    }

//    public SupportGetForUpdateResponseDto getSupportForUpdate(Long memberId, Long supportId) {
//        Support support = supportRepository.findSupportByIdWithFetch(supportId)
//                .orElseThrow(() -> new CustomException(ErrorCode.SUPPORT_NOT_FOUND));
//
//        if (support.getMember().getId() != memberId) {
//            throw new CustomException(ErrorCode.NO_ACCESS);
//        }
//
//        // 이미지를 Presigned URL로 처리
//        List<String> imageUrls = support.getImages().stream()
//                .map(supportImage -> fileService.getFileUrl(supportImage.getImageUrl()))
//                .toList();
//
//        return new SupportGetForUpdateResponseDto(
//                support.getMember().getId(),
//                support.getMember().getNickname(),
//                support.getTitle(),
//                support.getReason(),
//                support.getItem(),
//                support.getPurchaseUrl(),
//                support.getPrice(),
//                imageUrls,
//                support.getCheckPeriod(),
//                support.getCheckCount(),
//                support.getExpirationDate()
//        );
//    }
//
//    public void updateSupport(Long memberId, Long supportId, SupportUpdateRequestDto supportUpdateRequestDto) {
//        Support support = supportRepository.findSupportByIdWithFetch(supportId)
//                .orElseThrow(() -> new CustomException(ErrorCode.SUPPORT_NOT_FOUND));
//
//        if (support.getMember().getId() != memberId) {
//            throw new CustomException(ErrorCode.NO_ACCESS);
//        }
//        support.update(supportUpdateRequestDto);
//        support.getImages().clear();
//
//        List<SupportImage> newImages = supportUpdateRequestDto.getImages().stream()
//                .map(imageUrl -> new SupportImage(support, imageUrl))
//                .toList();
//
//        support.getImages().addAll(newImages);
//
//        // 5. 기존 이미지 삭제
//        support.getImages().clear(); // 기존 이미지 제거
//
//        // 6. 새로운 이미지 추가
//        support.getImages().addAll(newImages);
//    }
}
