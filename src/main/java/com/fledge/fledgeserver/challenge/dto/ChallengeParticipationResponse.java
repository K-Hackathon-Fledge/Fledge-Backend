package com.fledge.fledgeserver.challenge.dto;

import com.fledge.fledgeserver.challenge.entity.ChallengeParticipation;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@AllArgsConstructor
@Getter
@Schema(description = "챌린지 참여 응답 DTO")
public class ChallengeParticipationResponse {

    @Schema(description = "챌린지 참여 ID", example = "1")
    private Long id;

    @Schema(description = "회원 ID", example = "1")
    private Long memberId;

    @Schema(description = "챌린지 ID", example = "1")
    private Long challengeId;

    @Schema(description = "시작 날짜", example = "2023-01-01")
    private LocalDate startDate;

    @Schema(description = "종료 날짜", example = "2023-02-01")
    private LocalDate endDate;

    @Schema(description = "누락된 인증 횟수", example = "0")
    private int missedProofs;

    public ChallengeParticipationResponse(ChallengeParticipation participation) {
        this.id = participation.getId();
        this.memberId = participation.getMember().getId();
        this.challengeId = participation.getChallenge().getId();
        this.startDate = participation.getStartDate();
        this.endDate = participation.getEndDate();
    }

}
