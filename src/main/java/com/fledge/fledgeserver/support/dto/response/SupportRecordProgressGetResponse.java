package com.fledge.fledgeserver.support.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(description = "후원 게시글 조회 시 후원 기록 반환")
public class SupportRecordProgressGetResponse {

    @Schema(description = "후원 물품 총 금액", example = "1000000")
    private int totalPrice;

    @Schema(description = "현재 받은 후원금", example = "400000")
    private int supportedPrice;

    @Schema(description = "후원 진행률", example = "40")
    private double progress;


    public SupportRecordProgressGetResponse(int totalPrice, int supportedPrice) {
        this.totalPrice = totalPrice;
        this.supportedPrice = supportedPrice;
        // 진행률 계산
        double progressValue = (double) supportedPrice / totalPrice * 100;
        this.progress = progressValue;
    }
}
