package com.fledge.fledgeserver.challenge.service;

import com.fledge.fledgeserver.challenge.repository.ChallengeRepository;
import com.fledge.fledgeserver.challenge.Enum.Frequency;
import com.fledge.fledgeserver.challenge.dto.TopParticipantResponse;
import com.fledge.fledgeserver.challenge.entity.ChallengeParticipation;
import com.fledge.fledgeserver.challenge.repository.ChallengeParticipationRepository;
import com.fledge.fledgeserver.challenge.repository.ChallengeProofRepository;
import com.fledge.fledgeserver.challenge.dto.ChallengeParticipationResponse;
import com.fledge.fledgeserver.challenge.entity.Challenge;
import com.fledge.fledgeserver.challenge.entity.ChallengeProof;
import com.fledge.fledgeserver.exception.CustomException;
import com.fledge.fledgeserver.exception.ErrorCode;
import com.fledge.fledgeserver.member.entity.Member;
import com.fledge.fledgeserver.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChallengeParticipationService {

    private final ChallengeParticipationRepository participationRepository;
    private final ChallengeProofRepository proofRepository;
    private final MemberRepository memberRepository;
    private final ChallengeRepository challengeRepository;

    @Transactional
    public ChallengeParticipationResponse participateInChallenge(Long memberId, Long challengeId, LocalDate startDate) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        Challenge challenge = challengeRepository.findById(challengeId)
                .orElseThrow(() -> new CustomException(ErrorCode.CHALLENGE_NOT_FOUND));

        LocalDate endDate = startDate.plusWeeks(challenge.getPeriodWeeks());

        ChallengeParticipation participation = ChallengeParticipation.builder()
                .member(member)
                .challenge(challenge)
                .startDate(startDate)
                .endDate(endDate)
                .missedProofs(0)
                .build();

        participationRepository.save(participation);

        createProofRecords(participation, challenge.getFrequency());

        return new ChallengeParticipationResponse(participation);
    }

    private void createProofRecords(ChallengeParticipation participation, Frequency frequency) {
        LocalDate proofDate = participation.getStartDate();
        LocalDate endDate = participation.getEndDate();

        while (!proofDate.isAfter(endDate)) {
            ChallengeProof proof = ChallengeProof.builder()
                    .participation(participation)
                    .proofDate(proofDate)
                    .proofImageUrl(null)
                    .proofed(false)
                    .build();

            proofRepository.save(proof);

            proofDate = getNextProofDate(proofDate, frequency);
        }
    }

    private LocalDate getNextProofDate(LocalDate currentDate, Frequency frequency) {
        switch (frequency) {
            case DAILY:
                return currentDate.plusDays(1);
            case ONE_WEEK:
                return currentDate.plusWeeks(1);
            case TWO_WEEKS:
                return currentDate.plusWeeks(2);
            case FOUR_WEEKS:
                return currentDate.plusWeeks(4);
            default:
                throw new CustomException(ErrorCode.CHALLENGE_FREQUENCY_INVALID);
        }
    }

    @Transactional(readOnly = true)
    public List<TopParticipantResponse> getTopParticipants(int limit) {
        PageRequest pageable = PageRequest.of(0, limit);
        List<Object[]> topParticipants = participationRepository.findTopParticipants(pageable);

        return topParticipants.stream()
                .map(row -> {
                    Long memberId = (Long) row[0];
                    Long participationCount = (Long) row[1];
                    Long successCount = (Long) row[2];
                    Double successRate = ((Number) row[3]).doubleValue() * 100;

                    List<String> topCategories = participationRepository.findTopCategoriesByMemberId(memberId);

                    return new TopParticipantResponse(memberId, participationCount, successCount, successRate, topCategories);
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public void checkAndMarkChallengeSuccess() {
        List<ChallengeParticipation> participations = participationRepository.findAll();

        for (ChallengeParticipation participation : participations) {
            LocalDate endDate = participation.getEndDate();
            if (LocalDate.now().isAfter(endDate)) {
                List<ChallengeProof> proofs = proofRepository.findByParticipationId(participation.getId());
                long missedProofs = proofs.stream().filter(proof -> !proof.isProofed()).count();
                participation.updateMissedProofs((int) missedProofs);

                if (missedProofs == 0) {
                    participation.markAsSuccess();
                }
                participationRepository.save(participation);
            }
        }
    }

    @Transactional
    public void checkMissedProofs() {
        List<ChallengeProof> proofs = proofRepository.findAll();

        for (ChallengeProof proof : proofs) {
            if (!proof.isProofed() && proof.getProofDate().isBefore(LocalDate.now())) {
                ChallengeParticipation participation = proof.getParticipation();
                participation.incrementMissedProofs();
                participationRepository.save(participation);
            }
        }
    }
}

