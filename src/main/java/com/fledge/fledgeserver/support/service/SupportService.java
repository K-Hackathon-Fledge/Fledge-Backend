package com.fledge.fledgeserver.support.service;

import com.fledge.fledgeserver.exception.CustomException;
import com.fledge.fledgeserver.exception.ErrorCode;
import com.fledge.fledgeserver.file.FileService;
import com.fledge.fledgeserver.member.entity.Member;
import com.fledge.fledgeserver.member.entity.Role;
import com.fledge.fledgeserver.member.repository.MemberRepository;
import com.fledge.fledgeserver.support.dto.request.SupportRecordCreateRequest;
import com.fledge.fledgeserver.support.dto.request.SupportPostCreateRequest;
import com.fledge.fledgeserver.support.dto.response.SupportPostGetResponse;
import com.fledge.fledgeserver.support.dto.response.SupportRecordProgressGetResponse;
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
    public void createSupport(Long memberId, SupportPostCreateRequest supportPostCreateRequest) {
        Member member = memberRepository.findMemberById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        if (member.getRole() != Role.CANARY) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_REQUEST);
        }

        SupportPost supportPost = SupportPost.builder()
                .member(member)
                .supportPostCreateRequest(supportPostCreateRequest)
                .build();
        supportRepository.save(supportPost);

        for (String imageUrl : supportPostCreateRequest.getImages()) {
            SupportImage supportImage = SupportImage.builder()
                    .supportPost(supportPost)
                    .imageUrl(imageUrl)
                    .build();
            supportPost.getImages().add(supportImage);
        }
    }

    @Transactional(readOnly = true)
    public SupportPostGetResponse getSupport(Long supportId) {
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

        return new SupportPostGetResponse(
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
    public void createSupportRecord(Long supportId, SupportRecordCreateRequest supportRecordCreateRequest, Long memberId) {
        Member member = memberRepository.findMemberByIdOrThrow(memberId); // 후원자
        SupportPost supportPost = supportRepository.findSupportByIdOrThrow(supportId); // 게시글
        int amount = supportRecordCreateRequest.getAmount();
        List<SupportRecord> supportRecords = supportRecordRepository.findAllBySupportPost(supportPost);
        int beforeSupportPrice = supportRecords.stream()
                .mapToInt(SupportRecord::getAmount)
                .sum();

        // 검증 로직 (이번 후원 금액으로 후원 물품 가격 초과인 경우 예외 처리)
        if (amount + beforeSupportPrice > supportPost.getPrice()) {
            throw new CustomException(ErrorCode.OVER_SUPPORT_PRICE);
        }

        // 후원 내역 기록
        SupportRecord supportRecord = SupportRecord.builder()
                .member(member)
                .supportPost(supportPost)
                .bankName(supportRecordCreateRequest.getBankName())
                .bankCode(supportRecordCreateRequest.getBankCode())
                .account(supportRecordCreateRequest.getAccount())
                .amount(amount)
                .build();
        supportRecordRepository.save(supportRecord);

        // 후원 게시글 상태 변경
        supportPost.support();
    }

    @Transactional(readOnly = true)
    public SupportRecordProgressGetResponse getSupportProgress(Long supportId) {
        SupportPost supportPost = supportRepository.findSupportByIdWithFetch(supportId)
                .orElseThrow(() -> new CustomException(ErrorCode.SUPPORT_NOT_FOUND));

        List<SupportRecord> supportRecords = supportRecordRepository.findAllBySupportPost(supportPost);

        int totalPrice = supportPost.getPrice();

        // supportPrice: 지원 기록에서 총 지원 금액을 합산
        int supportPrice = supportRecords.stream()
                .mapToInt(SupportRecord::getAmount)
                .sum();

        return new SupportRecordProgressGetResponse(totalPrice, supportPrice);
    }
//    public SupportGetForUpdateResponse getSupportForUpdate(Long memberId, Long supportId) {
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
//        return new SupportGetForUpdateResponse(
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
//    public void updateSupport(Long memberId, Long supportId, SupportUpdateRequest supportUpdateRequestDto) {
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
