package com.fledge.fledgeserver.challenge.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "챌린지 인증 요청 DTO")
public class ChallengeProofRequest {

    @Schema(description = "인증 날짜", example = "2024-08-02")
    private LocalDate proofDate;

    @Schema(description = "인증 이미지 URL", example = "http://example.com/proof.jpg")
    private String proofImageUrl;
}