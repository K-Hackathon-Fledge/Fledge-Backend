package com.fledge.fledgeserver.challenge.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "인증 내역 상세 정보 DTO")
public class ProofDetailResponse {

    @Schema(description = "인증 상태", example = "true")
    private boolean status;

    @Schema(description = "인증 이미지 URL", example = "https://example.com/proof.jpg")
    private String proofImageUrl;

    @Schema(description = "인증 설명", example = "하루 운동 인증합니다!")
    private String description;

}
