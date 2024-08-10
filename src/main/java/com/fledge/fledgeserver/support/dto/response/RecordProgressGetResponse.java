package com.fledge.fledgeserver.support.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import java.text.DecimalFormat;

@Getter
@Schema(description = "후원 게시글 조회 시 후원 기록 반환")
public class RecordProgressGetResponse {

    private static final DecimalFormat df = new DecimalFormat("#.#");

    @Schema(description = "후원 물품 총 금액", example = "1000000")
    private int totalPrice;

    @Schema(description = "현재 받은 후원금", example = "400000")
    private int supportedPrice;

    @Schema(description = "후원 진행률(%)", example = "40")
    private double progress;

    public RecordProgressGetResponse(int totalPrice, int supportedPrice) {
        this.totalPrice = totalPrice;
        this.supportedPrice = supportedPrice;
        if (totalPrice == 0) {
            this.progress = 0.0;
        } else {
            double progressValue = (double) supportedPrice / totalPrice * 100;
            this.progress = Double.parseDouble(df.format(progressValue));
        }
    }
}
