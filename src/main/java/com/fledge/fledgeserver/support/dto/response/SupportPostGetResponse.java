package com.fledge.fledgeserver.support.dto.response;

import com.fledge.fledgeserver.common.utils.TimeUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;

@Getter
@Schema(description = "후원하기 게시글 조회 DTO")
public class SupportPostGetResponse {

    @Schema(description = "멤버 식별자(프로필 팝업 띄우기)", example = "2")
    private Long memberId;

    //    @Schema(description = "", example = "")
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

    @Schema(description = "후원 물품 이미지 리스트")
    private List<String> images;

    @Schema(description = "남은 기간", example = "99")
    private String leftDays;

    @Schema(description = "후원 만료 시점", example = "2024-07-31")
    private String expirationDate;

    @Schema(description = "후원자 리스트(후원자 + 금액)", example = "[{\"라이언고슬밥\": 10000}, {\"명륜진샤오미\": 20000}]")
    private List<Map<String, Integer>> supporterList;

    public SupportPostGetResponse(Long memberId, String nickname, String title, String reason, String item, String purchaseUrl, int price, List<String> images, LocalDate expirationDate, List<Map<String, Integer>> supporterList) {
        this.memberId = memberId;
        this.nickname = nickname;
        this.title = title;
        this.reason = reason;
        this.item = item;
        this.purchaseUrl = purchaseUrl;
        this.price = price;
        this.images = images;
        this.leftDays = String.valueOf(ChronoUnit.DAYS.between(LocalDate.now(), expirationDate));
        this.expirationDate = TimeUtils.refineToDate(expirationDate);
        this.supporterList = supporterList;
    }
}
