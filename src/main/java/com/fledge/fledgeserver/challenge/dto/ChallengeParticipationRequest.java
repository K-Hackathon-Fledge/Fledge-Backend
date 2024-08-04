package com.fledge.fledgeserver.challenge.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class ChallengeParticipationRequest {

    private Long memberId;
    private Long challengeId;
    private LocalDate startDate;
}

