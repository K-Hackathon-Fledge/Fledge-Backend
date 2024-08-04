package com.fledge.fledgeserver.challenge.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class ChallengeProofRequest {

    private LocalDate proofDate;
    private String proofImageUrl;
}
