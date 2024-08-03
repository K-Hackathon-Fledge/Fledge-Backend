package com.fledge.fledgeserver.support.service;

import com.fledge.fledgeserver.exception.CustomException;
import com.fledge.fledgeserver.exception.ErrorCode;
import com.fledge.fledgeserver.file.FileService;
import com.fledge.fledgeserver.member.entity.Member;
import com.fledge.fledgeserver.member.entity.Role;
import com.fledge.fledgeserver.member.repository.MemberRepository;
import com.fledge.fledgeserver.support.dto.request.SupportRecordCreateRequestDto;
import com.fledge.fledgeserver.support.dto.request.SupportPostCreateRequestDto;
import com.fledge.fledgeserver.support.dto.response.SupportPostGetResponseDto;
import com.fledge.fledgeserver.support.dto.response.SupportRecordProgressGetResponseDto;
import com.fledge.fledgeserver.support.entity.SupportPost;
import com.fledge.fledgeserver.support.entity.SupportImage;
import com.fledge.fledgeserver.support.entity.SupportRecord;
import com.fledge.fledgeserver.support.repository.SupportRecordRepository;
import com.fledge.fledgeserver.support.repository.SupportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SupportService {
    private final MemberRepository memberRepository;
    private final SupportRepository supportRepository;
    private final FileService fileService;
    private final SupportRecordRepository supportRecordRepository;

    @Transactional
    public void createSupport(Long memberId, SupportPostCreateRequestDto supportPostCreateRequestDto) {
        Member member = memberRepository.findMemberById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        if (member.getRole() != Role.CANARY) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_REQUEST);
        }

        SupportPost supportPost = SupportPost.builder()
                .member(member)
                .supportPostCreateRequestDto(supportPostCreateRequestDto)
                .build();
        supportRepository.save(supportPost);

        for (String imageUrl : supportPostCreateRequestDto.getImages()) {
            SupportImage supportImage = SupportImage.builder()
                    .supportPost(supportPost)
                    .imageUrl(imageUrl)
                    .build();
            supportPost.getImages().add(supportImage);
        }
    }

    @Transactional(readOnly = true)
    public SupportPostGetResponseDto getSupport(Long supportId) {
        SupportPost supportPost = supportRepository.findSupportByIdWithFetch(supportId)
                .orElseThrow(() -> new CustomException(ErrorCode.SUPPORT_NOT_FOUND));

        List<SupportRecord> supportRecords = supportRecordRepository.findAllBySupportPost(supportPost);

        List<Map<String, Integer>> supporterList = supportRecords.stream()
                .collect(Collectors.groupingBy(
                        record -> record.getMember().getNickname(), // 후원자 닉네임
                        Collectors.summingInt(SupportRecord::getAmount) // 해당 닉네임의 총 후원 금액
                ))
                .entrySet().stream() // Map.Entry로 변환
                .map(entry -> Map.of(entry.getKey(), entry.getValue())) // 각 엔트리를 Map으로 변환
                .collect(Collectors.toList()); // 최종 리스트로 수집

        return new SupportPostGetResponseDto(
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
                supportPost.getExpirationDate(),
                supporterList
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

    @Transactional(readOnly = true)
    public SupportRecordProgressGetResponseDto getSupportProgress(Long supportId) {
        SupportPost supportPost = supportRepository.findSupportByIdWithFetch(supportId)
                .orElseThrow(() -> new CustomException(ErrorCode.SUPPORT_NOT_FOUND));

        List<SupportRecord> supportRecords = supportRecordRepository.findAllBySupportPost(supportPost);

        int totalPrice = supportPost.getPrice();

        // supportPrice: 지원 기록에서 총 지원 금액을 합산
        int supportPrice = supportRecords.stream()
                .mapToInt(SupportRecord::getAmount)
                .sum();

        return new SupportRecordProgressGetResponseDto(totalPrice, supportPrice);
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
