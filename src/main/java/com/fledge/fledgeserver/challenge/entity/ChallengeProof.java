package com.fledge.fledgeserver.challenge.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChallengeProof {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "participation_id", nullable = false)
    private ChallengeParticipation participation;

    private LocalDate proofDate;

    @Lob
    private String proofImageUrl;

    private boolean proofed;

    @Builder
    public ChallengeProof(ChallengeParticipation participation, LocalDate proofDate, String proofImageUrl, boolean proofed) {
        this.participation = participation;
        this.proofDate = proofDate;
        this.proofImageUrl = proofImageUrl;
        this.proofed = proofed;
    }

    public void markAsProofed() {
        this.proofed = true;
    }

    public void updateProofImageUrl(String proofImageUrl) {
        this.proofImageUrl = proofImageUrl;
    }
}
