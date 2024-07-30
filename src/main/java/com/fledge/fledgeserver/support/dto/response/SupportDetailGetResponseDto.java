package com.fledge.fledgeserver.support.dto.response;

import com.fledge.fledgeserver.support.entity.Support;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Schema(description = "후원하기 게시글 조회 DTO")
public class SupportDetailGetResponseDto {

    @Schema(description = "후원 게시글 ID", example = "1")
    private Long id;

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

    @Schema(description = "후원 물품 이미지 리스트")
    private List<String> images; // 이미지 URL을 String List로 표현

    @Schema(description = "후원 인증 기간", example = "30")
    private int checkPeriod;

    @Schema(description = "후원 인증 횟수", example = "1")
    private int checkCount;

    @Schema(description = "만료 시점", example = "2024-12-31")
    private LocalDate expirationDate;

    public SupportDetailGetResponseDto(Support support, List<String> presignedImageUrl) {
        this.id = support.getId();
        this.title = support.getTitle();
        this.reason = support.getReason();
        this.item = support.getItem();
        this.purchaseUrl = support.getPurchaseUrl();
        this.price = support.getPrice();
        this.images = presignedImageUrl;
        this.checkPeriod = support.getCheckPeriod();
        this.checkCount = support.getCheckCount();
        this.expirationDate = support.getExpirationDate();
    }
}