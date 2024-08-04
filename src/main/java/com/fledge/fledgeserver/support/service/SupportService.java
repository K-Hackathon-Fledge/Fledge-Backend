package com.fledge.fledgeserver.support.service;

import com.fledge.fledgeserver.exception.CustomException;
import com.fledge.fledgeserver.exception.ErrorCode;
import com.fledge.fledgeserver.file.FileService;
import com.fledge.fledgeserver.member.entity.Member;
import com.fledge.fledgeserver.member.entity.Role;
import com.fledge.fledgeserver.member.repository.MemberRepository;
import com.fledge.fledgeserver.support.dto.request.PostUpdateRequest;
import com.fledge.fledgeserver.support.dto.request.RecordCreateRequest;
import com.fledge.fledgeserver.support.dto.request.PostCreateRequest;
import com.fledge.fledgeserver.support.dto.response.*;
import com.fledge.fledgeserver.support.entity.*;
import com.fledge.fledgeserver.support.repository.SupportRecordRepository;
import com.fledge.fledgeserver.support.repository.SupportPostRepository;
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
    private final SupportPostRepository supportPostRepository;
    private final FileService fileService;
    private final SupportRecordRepository supportRecordRepository;

    @Transactional
    public void createSupport(Long memberId, PostCreateRequest postCreateRequest) {
        Member member = memberRepository.findMemberById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        if (member.getRole() != Role.CANARY) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_REQUEST);
        }

        SupportPost supportPost = SupportPost.builder()
                .member(member)
                .postCreateRequest(postCreateRequest)
                .build();
        supportPostRepository.save(supportPost);

        for (String imageUrl : postCreateRequest.getImages()) {
            SupportImage supportImage = SupportImage.builder()
                    .supportPost(supportPost)
                    .imageUrl(imageUrl)
                    .build();
            supportPost.getImages().add(supportImage);
        }
    }


    @Transactional(readOnly = true)
    public PostGetResponse getSupport(Long supportId) {
        SupportPost supportPost = supportPostRepository.findSupportByIdWithFetch(supportId)
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

        return new PostGetResponse(
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
    public void createSupportRecord(Long supportId, RecordCreateRequest recordCreateRequest, Long memberId) {
        Member member = memberRepository.findMemberByIdOrThrow(memberId); // 후원자
        SupportPost supportPost = supportPostRepository.findSupportByIdOrThrow(supportId); // 게시글
        int amount = recordCreateRequest.getAmount();
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
                .bankName(recordCreateRequest.getBankName())
                .bankCode(recordCreateRequest.getBankCode())
                .account(recordCreateRequest.getAccount())
                .amount(amount)
                .build();
        supportRecordRepository.save(supportRecord);

        // 후원 게시글 상태 변경
        supportPost.support();
    }

    @Transactional(readOnly = true)
    public RecordProgressGetResponse getSupportProgress(Long supportId) {
        SupportPost supportPost = supportPostRepository.findSupportByIdWithFetch(supportId)
                .orElseThrow(() -> new CustomException(ErrorCode.SUPPORT_NOT_FOUND));

        List<SupportRecord> supportRecords = supportRecordRepository.findAllBySupportPost(supportPost);

        int totalPrice = supportPost.getPrice();

        // supportPrice: 지원 기록에서 총 지원 금액을 합산
        int supportPrice = supportRecords.stream()
                .mapToInt(SupportRecord::getAmount)
                .sum();

        return new RecordProgressGetResponse(totalPrice, supportPrice);
    }

    @Transactional(readOnly = true)
    public PostGetForUpdateResponse getSupportForUpdate(Long supportId, Long memberId) {
        SupportPost supportPost = supportPostRepository.findSupportByIdWithFetch(supportId)
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

        return new PostGetForUpdateResponse(supportId, String.valueOf(supportCategory), supportPostStatus, MemberId, nickname, title, reason, item, purchaseUrl, price, images, promise, expirationDate, bank, account, recipientName, phone, address, detailAddress, zip);
    }

    @Transactional
    public void updateSupportPost(Long memberId, Long supportId, PostUpdateRequest postUpdateRequestDto) {
        SupportPost supportPost = supportPostRepository.findSupportByIdWithFetch(supportId)
                .orElseThrow(() -> new CustomException(ErrorCode.SUPPORT_NOT_FOUND));
        if (supportPost.getMember().getId() != memberId) {
            throw new CustomException(ErrorCode.NO_ACCESS);
        }

        if ("PENDING".equals(supportPost.getSupportPostStatus())) {
            supportPost.updateAll(postUpdateRequestDto);

            supportPost.getImages().clear();
            List<SupportImage> newImages = postUpdateRequestDto.getImages().stream()
                    .map(imageUrl -> new SupportImage(supportPost, imageUrl))
                    .toList();
            supportPost.getImages().addAll(newImages);
        } else {
            supportPost.updateNotPending(postUpdateRequestDto);
        }
    }

    @Transactional(readOnly = true)
    public PostTotalPagingResponse pagingSupportPost(int page, List<String> category, String q) {
        // 첫 번째 페이지 인덱스: 0, limit = 9 고정
        PageRequest pageable = PageRequest.of(page, 9);

        List<SupportCategory> selectedCategories = category.isEmpty() ? null : category.stream()
                .map(SupportCategory::valueOf)
                .collect(Collectors.toList());

        Page<SupportPost> supportPostPage = supportPostRepository.findByCategoryAndSearch(selectedCategories, q, pageable);

        List<PostPagingResponse> postPagingResponse = supportPostPage.getContent().stream()
                .map(supportPost -> {
                    int totalPrice = supportPost.getPrice();
                    int supportedPrice = supportRecordRepository.sumSupportedPriceBySupportPostId(supportPost.getId());

                    RecordProgressGetResponse supportRecordProgress = new RecordProgressGetResponse(totalPrice, supportedPrice);

                    return new PostPagingResponse(
                            supportPost.getId(),
                            supportPost.getTitle(),
                            supportPost.getExpirationDate(),
                            supportRecordProgress
                    );
                })
                .collect(Collectors.toList());
        int totalPages = supportPostPage.getTotalPages();
        return new PostTotalPagingResponse(totalPages, postPagingResponse);
    }
}
