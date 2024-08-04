package com.fledge.fledgeserver.support.dto.response;

import com.fledge.fledgeserver.common.utils.TimeUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Schema(description = "후원하기 게시글 수정 시 기존 데이터 조회 DTO")
public class SupportGetForUpdateResponse {

    @Schema(description = "게시글 ID", example = "2")
    private Long supportPostId;

    @Schema(description = "게시글 카테고리", example = "MEDICAL")
    private String supportCategory;

    @Schema(description = "후원금 받은적 있는지 없는지", example = "PENDING")
    private String supportPostStatus;

    @Schema(description = "멤버 식별자(프로필 팝업 띄우기)", example = "2")
    private Long memberId;

    @Schema(description = "작성자 닉네임", example = "카드값줘체리")
    private String nickname;

    @Schema(description = "후원 게시글 제목", example = "후원 요청")
    private String title;

    @Schema(description = "후원 필요한 이유", example = "자립을 위한 후원")
    private String reason;

    @Schema(description = "후원 물품 명", example = "노트북")
    private String item;

    @Schema(description = "구매 URL", example = "https://example.com/product/1")
    private String purchaseUrl;

    @Schema(description = "후원 물품 가격", example = "500000")
    private int price;

    @Schema(description = "후원 물품 이미지 리스트", example = "WEEKLY")
    private List<String> images;

    @Schema(description = "후원자와의 약속 타입")
    private String promise;

    @Schema(description = "후원 만료 시점", example = "2024-07-31")
    private String expirationDate;

    // 추가 필드: 의료비 및 법률 관련
    @Schema(description = "은행 이름", example = "우리은행")
    private String bank;

    @Schema(description = "계좌 번호", example = "123-456-789012")
    private String account;

    // 추가 필드: 기타 카테고리 관련
    @Schema(description = "수령인 이름", example = "홍길동")
    private String recipientName;

    @Schema(description = "전화번호", example = "010-1234-5678")
    private String phone;

    @Schema(description = "주소", example = "서울시 강남구")
    private String address;

    @Schema(description = "상세 주소", example = "123-45")
    private String detailAddress;

    @Schema(description = "우편번호", example = "06123")
    private String zip;

    public SupportGetForUpdateResponse(Long supportPostId, String supportCategory, String supportPostStatus,Long memberId, String nickname, String title, String reason, String item, String purchaseUrl, int price, List<String> images, String promise, LocalDate expirationDate, String bank, String account, String recipientName, String phone, String address, String detailAddress, String zip) {
        this.supportPostId = supportPostId;
        this.supportCategory = supportCategory;
        this.supportPostStatus = supportPostStatus;
        this.memberId = memberId;
        this.nickname = nickname;
        this.title = title;
        this.reason = reason;
        this.item = item;
        this.purchaseUrl = purchaseUrl;
        this.price = price;
        this.images = images;
        this.promise = promise;
        this.expirationDate = TimeUtils.refineToDate(expirationDate);
        this.bank = bank;
        this.account = account;
        this.recipientName = recipientName;
        this.phone = phone;
        this.address = address;
        this.detailAddress = detailAddress;
        this.zip = zip;
    }
}
