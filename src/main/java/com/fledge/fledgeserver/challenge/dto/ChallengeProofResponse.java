package com.fledge.fledgeserver.challenge.dto;

import com.fledge.fledgeserver.challenge.entity.ChallengeProof;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@AllArgsConstructor
@Getter
public class ChallengeProofResponse {

    private Long id;
    private Long participationId;
    private LocalDate proofDate;
    private String proofImageUrl;
    private boolean proofed;

    public ChallengeProofResponse(ChallengeProof proof) {
        this.id = proof.getId();
        this.participationId = proof.getParticipation().getId();
        this.proofDate = proof.getProofDate();
        this.proofImageUrl = proof.getProofImageUrl();
        this.proofed = proof.isProofed();
    }

}
