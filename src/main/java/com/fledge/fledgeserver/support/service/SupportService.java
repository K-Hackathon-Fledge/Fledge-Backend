package com.fledge.fledgeserver.support.service;

import com.fledge.fledgeserver.exception.CustomException;
import com.fledge.fledgeserver.exception.ErrorCode;
import com.fledge.fledgeserver.file.FileService;
import com.fledge.fledgeserver.member.entity.Member;
import com.fledge.fledgeserver.member.entity.Role;
import com.fledge.fledgeserver.member.repository.MemberRepository;
import com.fledge.fledgeserver.support.dto.request.SupportPostUpdateRequest;
import com.fledge.fledgeserver.support.dto.request.SupportRecordCreateRequest;
import com.fledge.fledgeserver.support.dto.request.SupportPostCreateRequest;
import com.fledge.fledgeserver.support.dto.response.SupportGetForUpdateResponse;
import com.fledge.fledgeserver.support.dto.response.SupportPostGetResponse;
import com.fledge.fledgeserver.support.dto.response.SupportPostPagingResponse;
import com.fledge.fledgeserver.support.dto.response.SupportRecordProgressGetResponse;
import com.fledge.fledgeserver.support.entity.*;
import com.fledge.fledgeserver.support.repository.SupportRecordRepository;
import com.fledge.fledgeserver.support.repository.SupportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
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
                supportId,
                supportPost.getMember().getId(),
                supportPost.getMember().getNickname(),
                supportPost.getTitle(),
                supportPost.getReason(),
                supportPost.getItem(),
                supportPost.getPurchaseUrl(),
                supportPost.getPrice(),
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

    @Transactional(readOnly = true)
    public SupportGetForUpdateResponse getSupportForUpdate(Long supportId, Long memberId) {
        SupportPost supportPost = supportRepository.findSupportByIdWithFetch(supportId)
                .orElseThrow(() -> new CustomException(ErrorCode.SUPPORT_NOT_FOUND));

        if (!supportPost.getMember().getId().equals(memberId)) {
            throw new CustomException(ErrorCode.NO_ACCESS);
        }

        // DTO에 필요한 정보 추출 -> Category 기준으로 프론트에서 막음
        SupportCategory supportCategory = supportPost.getSupportCategory();
        String supportPostStatus = String.valueOf(supportPost.getSupportPostStatus());
        Long MemberId = supportPost.getMember().getId();
        String nickname = supportPost.getMember().getNickname();
        String title = supportPost.getTitle();
        String reason = supportPost.getReason();
        String item = supportPost.getItem();
        String purchaseUrl = supportPost.getPurchaseUrl();
        int price = supportPost.getPrice();
        List<String> images = supportPost.getImages().stream()
                .map(supportImage -> fileService.getFileUrl(supportImage.getImageUrl()))
                .toList();
        String promise = String.valueOf(supportPost.getPromise());
        LocalDate expirationDate = supportPost.getExpirationDate();

        // 카테고리에 따라 응답 데이터 설정
        String bank = null;
        String account = null;

        String recipientName = null;
        String phone = null;
        String address = null;
        String detailAddress = null;
        String zip = null;

        if (supportCategory == SupportCategory.MEDICAL || supportCategory == SupportCategory.LEGAL_AID) {
            bank = supportPost.getBank();
            account = supportPost.getAccount();
        } else {
            recipientName = supportPost.getRecipientName();
            phone = supportPost.getPhone();
            address = supportPost.getAddress();
            detailAddress = supportPost.getDetailAddress();
            zip = supportPost.getZip();
        }

        return new SupportGetForUpdateResponse(supportId, String.valueOf(supportCategory), supportPostStatus, MemberId, nickname, title, reason, item, purchaseUrl, price, images, promise, expirationDate, bank, account, recipientName, phone, address, detailAddress, zip);
    }

    @Transactional
    public void updateSupportPost(Long memberId, Long supportId, SupportPostUpdateRequest supportPostUpdateRequestDto) {
        SupportPost supportPost = supportRepository.findSupportByIdWithFetch(supportId)
                .orElseThrow(() -> new CustomException(ErrorCode.SUPPORT_NOT_FOUND));
        if (supportPost.getMember().getId() != memberId) {
            throw new CustomException(ErrorCode.NO_ACCESS);
        }

        if ("PENDING".equals(supportPost.getSupportPostStatus())) {
            supportPost.updateAll(supportPostUpdateRequestDto);

            supportPost.getImages().clear();
            List<SupportImage> newImages = supportPostUpdateRequestDto.getImages().stream()
                    .map(imageUrl -> new SupportImage(supportPost, imageUrl))
                    .toList();
            supportPost.getImages().addAll(newImages);
        } else {
            supportPost.updateNotPending(supportPostUpdateRequestDto);
        }
    }

    @Transactional(readOnly = true)
    public List<SupportPostPagingResponse> pagingSupportPost(int page, List<String> category, String q) {
        PageRequest pageable = PageRequest.of(page, 9); // limit 9개

        List<SupportCategory> selectedCategories = category.isEmpty() ? null : category.stream()
                .map(SupportCategory::valueOf) // String을 SupportCategory로 변환
                .collect(Collectors.toList());

        Page<SupportPost> supportPostPage = supportRepository.findByCategoryAndSearch(selectedCategories, q, pageable);

        return supportPostPage.getContent().stream()
                .map(supportPost -> {
                    // 총 금액과 현재 지원된 금액 가져오기 (예시)
                    int totalPrice = supportPost.getPrice(); // 총 금액을 가져오는 메서드가 필요
                    int supportedPrice = supportRecordRepository.sumSupportedPriceBySupportPostId(supportPost.getId()); // 지원 금액을 합산하는 메서드 필요

                    SupportRecordProgressGetResponse supportRecordProgress = new SupportRecordProgressGetResponse(totalPrice, supportedPrice);

                    return new SupportPostPagingResponse(
                            supportPost.getId(),
                            supportPost.getTitle(),
                            supportPost.getExpirationDate(),
                            supportRecordProgress
                    );
                })
                .collect(Collectors.toList());
    }
}
