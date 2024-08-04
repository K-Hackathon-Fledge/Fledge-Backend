package com.fledge.fledgeserver.challenge.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "챌린지 참여 요청 DTO")
public class ChallengeParticipationRequest {

    @Schema(description = "회원 ID", example = "1")
    private Long memberId;

    @Schema(description = "챌린지 ID", example = "1")
    private Long challengeId;

    @Schema(description = "시작 날짜", example = "2024-08-01")
    private LocalDate startDate;
}