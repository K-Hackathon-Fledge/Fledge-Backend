package com.fledge.fledgeserver.challenge.dto;

import com.fledge.fledgeserver.challenge.entity.ChallengeParticipation;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@AllArgsConstructor
@Getter
public class ChallengeParticipationResponse {

    private Long id;
    private Long memberId;
    private Long challengeId;
    private LocalDate startDate;
    private LocalDate endDate;
    private int missedProofs;

    public ChallengeParticipationResponse(ChallengeParticipation participation) {
        this.id = participation.getId();
        this.memberId = participation.getMember().getId();
        this.challengeId = participation.getChallenge().getId();
        this.startDate = participation.getStartDate();
        this.endDate = participation.getEndDate();
    }

}
