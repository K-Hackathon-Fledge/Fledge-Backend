package com.fledge.fledgeserver.support.service;

import com.fledge.fledgeserver.exception.CustomException;
import com.fledge.fledgeserver.exception.ErrorCode;
import com.fledge.fledgeserver.file.FileService;
import com.fledge.fledgeserver.member.entity.Member;
import com.fledge.fledgeserver.member.entity.Role;
import com.fledge.fledgeserver.member.repository.MemberRepository;
import com.fledge.fledgeserver.support.dto.request.PostCreateRequest;
import com.fledge.fledgeserver.support.dto.request.PostUpdateRequest;
import com.fledge.fledgeserver.support.dto.request.RecordCreateRequest;
import com.fledge.fledgeserver.support.dto.response.*;
import com.fledge.fledgeserver.support.entity.*;
import com.fledge.fledgeserver.support.repository.SupportImageRepository;
import com.fledge.fledgeserver.support.repository.SupportPostRepository;
import com.fledge.fledgeserver.support.repository.SupportRecordRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.internal.util.stereotypes.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.fledge.fledgeserver.support.entity.SupportPostStatus.IN_PROGRESS;
import static com.fledge.fledgeserver.support.entity.SupportPostStatus.PENDING;

@Service
@RequiredArgsConstructor
public class SupportService {
    private final FileService fileService;
    private final MemberRepository memberRepository;
    private final SupportPostRepository supportPostRepository;
    private final SupportRecordRepository supportRecordRepository;
    private final SupportImageRepository supportImageRepository;
    @Lazy
    private static final List<SupportPostStatus> notPossibleStatus = Arrays.asList(
            SupportPostStatus.TERMINATED,
            SupportPostStatus.COMPLETED
    );

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
                        .map(supportImage -> fileService.getDownloadPresignedUrl(supportImage.getImageUrl()))
                        .toList(),
                supportPost.getExpirationDate(),
                supporterList
        );
    }

    @Transactional
    public void createSupportRecord(Long supportId, RecordCreateRequest recordCreateRequest, Long memberId) {
        SupportPost supportPost = supportPostRepository.findSupportByIdOrThrow(supportId); // 게시글

        if (notPossibleStatus.contains(supportPost.getSupportPostStatus())) {
            // "COMPLETED", "TERMINATED" 상태인 경우 후원 불가
            throw new CustomException(ErrorCode.NOT_SUPPORTED_STATUS);
        }

        Member member = memberRepository.findMemberByIdOrThrow(memberId); // 후원자
        int amount = recordCreateRequest.getAmount();
        List<SupportRecord> supportRecords = supportRecordRepository.findAllBySupportPost(supportPost);
        int beforeSupportPrice = supportRecords.stream()
                .mapToInt(SupportRecord::getAmount)
                .sum();

        int afterSupport = amount + beforeSupportPrice;
        int itemPrice = supportPost.getPrice();

        if (afterSupport > itemPrice) {
            // 검증 로직 (이번 후원 금액으로 후원 물품 가격 초과인 경우 예외 처리)
            throw new CustomException(ErrorCode.OVER_SUPPORT_PRICE);
        } else if (afterSupport == itemPrice) {
            SupportRecord supportRecord = SupportRecord.builder()
                    .member(member)
                    .supportPost(supportPost)
                    .bankName(recordCreateRequest.getBankName())
                    .bankCode(recordCreateRequest.getBankCode())
                    .account(recordCreateRequest.getAccount())
                    .amount(amount)
                    .build();
            supportRecordRepository.save(supportRecord);
            supportPost.setCompleted(); // 후원 물품 금액 달성 시 게시글 상태 "COMPLETED"로 처리
        } else {
            SupportRecord supportRecord = SupportRecord.builder()
                    .member(member)
                    .supportPost(supportPost)
                    .bankName(recordCreateRequest.getBankName())
                    .bankCode(recordCreateRequest.getBankCode())
                    .account(recordCreateRequest.getAccount())
                    .amount(amount)
                    .build();
            supportRecordRepository.save(supportRecord);
            supportPost.support();
        }
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
      
        List<Map<String, String>> images = supportPost.getImages().stream()
                .map(supportImage -> {
                    Map<String, String> imageMap = new HashMap<>();
                    imageMap.put("originalUrl", supportImage.getImageUrl());
                    imageMap.put("presignedUrl", fileService.getDownloadPresignedUrl(supportImage.getImageUrl())); // 변환된 URL
                    return imageMap; // Map 반환
                })
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

        if (supportCategory == SupportCategory.MEDICAL || supportCategory == SupportCategory.LEGAL_AID || supportCategory == SupportCategory.EDUCATION) {
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

        if (!supportPost.getMember().getId().equals(memberId)) {
            throw new CustomException(ErrorCode.NO_ACCESS);
        }
        if (PENDING.equals(supportPost.getSupportPostStatus())) {
            supportPost.updateAll(postUpdateRequestDto);
            clearAndUpdateImages(supportPost, postUpdateRequestDto);
        } else {
            supportPost.updateNotPending(postUpdateRequestDto);
        }
    }

    private void clearAndUpdateImages(SupportPost supportPost, PostUpdateRequest postUpdateRequestDto) {
        supportPost.getImages().clear();

        List<SupportImage> newImages = postUpdateRequestDto.getImages().stream()
                .map(imageUrl -> new SupportImage(supportPost, imageUrl))
                .toList();

        supportPost.getImages().addAll(newImages);
    }


    @Transactional(readOnly = true)
    public PostTotalPagingResponse pagingSupportPost(int page, String q, List<String> category, String status) {
        PageRequest pageable = PageRequest.of(page, 9); // 페이지 번호가 0부터 시작

        List<SupportCategory> selectedCategories = category.isEmpty() ? null : category.stream()
                .map(SupportCategory::valueOf)
                .collect(Collectors.toList());

        // 쿼리를 통해 페이징된 결과를 가져옵니다.
        Page<SupportPost> supportPostPage = supportPostRepository.findByCategoryAndSearchAndSupportPostStatusWithImages(selectedCategories, q, status, pageable);
        // 결과가 없을 경우 빈 리스트 반환
        if (supportPostPage.isEmpty()) {
            return new PostTotalPagingResponse(0, 0, Collections.emptyList());
        }

        long totalElements = supportPostPage.getTotalElements();

        List<PostPagingResponse> postPagingResponse = supportPostPage.getContent().stream()
                .map(supportPost -> {
                    int totalPrice = supportPost.getPrice();
                    int supportedPrice = supportPost.getSupportRecords().stream()
                            .mapToInt(SupportRecord::getAmount)
                            .sum();
                    
                    RecordProgressGetResponse supportRecordProgress = new RecordProgressGetResponse(totalPrice, supportedPrice);
                    String imageUrl = supportPost.getImages().isEmpty() ? null : fileService.getDownloadPresignedUrl(supportPost.getImages().get(0).getImageUrl());
                    System.out.println("imageUrl = " + imageUrl);
                    return new PostPagingResponse(
                            supportPost.getId(),
                            supportPost.getTitle(),
                            supportPost.getExpirationDate(),
                            imageUrl,
                            supportRecordProgress
                    );
                })
                .collect(Collectors.toList());

        int totalPages = supportPostPage.getTotalPages(); // 총 페이지 수 계산

        return new PostTotalPagingResponse((int) totalElements, totalPages, postPagingResponse);
    }


    @Transactional(readOnly = true)
    public List<PostPagingResponse> deadlineApproachingPosts() {
        List<SupportPost> supportPosts = supportPostRepository.findByExpirationDateWithinSevenDays(PENDING, IN_PROGRESS);

        List<PostPagingResponse> supportPostsList = supportPosts.stream()
                .map(supportPost -> {
                    int totalPrice = supportPost.getPrice();
                    Long supportPostId = supportPost.getId();

                    int supportedPrice = supportRecordRepository.sumSupportedPriceBySupportPostId(supportPostId);

                    RecordProgressGetResponse supportRecordProgress = new RecordProgressGetResponse(totalPrice, supportedPrice);

                    SupportImage supportImage = supportImageRepository.findFirstImageBySupportPostIdOrDefault(supportPostId);
                    
                    String imageUrl = (supportImage != null) ? fileService.getDownloadPresignedUrl(supportImage.getImageUrl()) : null; // Set to null if no image found

                    return new PostPagingResponse(
                            supportPostId,
                            supportPost.getTitle(),
                            supportPost.getExpirationDate(),
                            imageUrl,
                            supportRecordProgress
                    );
                })
                .collect(Collectors.toList());

        return supportPostsList;
    }

    @Transactional
    public void checkAndExpireSupportPosts() {
        LocalDate currentDate = LocalDate.now();
        List<SupportPost> supportPosts = supportPostRepository.findAllBySupportPostStatusOr();

        supportPosts.stream()
                .filter(supportPost -> supportPost.getExpirationDate().isBefore(currentDate))
                .forEach(supportPost -> {
                    supportPost.setExpiration();
                    supportPostRepository.save(supportPost);
                });
    }

    @Transactional
    public void deleteSupportPost(Long memberId, Long supportId) {
        SupportPost supportPost = supportPostRepository.findSupportByIdOrThrow(supportId);
        if (!supportPost.getMember().getId().equals(memberId)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_REQUEST);
        }
        if (notPossibleStatus.contains(supportPost.getSupportPostStatus())) {
            throw new CustomException(ErrorCode.NOT_SUPPORTED_STATUS);
        }

        LocalDateTime now = LocalDateTime.now();

        List<Long> imageIds = supportPost.getImages().stream()
                .map(SupportImage::getId)
                .collect(Collectors.toList());
        List<Long> recordIds = supportPost.getSupportRecords().stream()
                .map(SupportRecord::getId)
                .collect(Collectors.toList());

        supportPost.softDelete(now);
        supportImageRepository.softDeleteByIds(imageIds, now);
        supportRecordRepository.softDeleteByIds(recordIds, now);
    }
}
