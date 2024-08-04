package com.fledge.fledgeserver.challenge.entity;

import com.fledge.fledgeserver.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChallengeParticipation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "challenge_id", nullable = false)
    private Challenge challenge;

    private LocalDate startDate;

    private LocalDate endDate;

    private int missedProofs;

    private boolean isSuccess;

    @Builder
    public ChallengeParticipation(Member member, Challenge challenge, LocalDate startDate, LocalDate endDate, int missedProofs, boolean isSuccess) {
        this.member = member;
        this.challenge = challenge;
        this.startDate = startDate;
        this.endDate = endDate;
        this.missedProofs = missedProofs;
        this.isSuccess = isSuccess;
    }

    public void incrementMissedProofs() {
        this.missedProofs++;
    }

    public void markAsSuccess() {
        this.isSuccess = true;
    }

    public void updateMissedProofs(int missedProofs) {
        this.missedProofs = missedProofs;
    }
}

