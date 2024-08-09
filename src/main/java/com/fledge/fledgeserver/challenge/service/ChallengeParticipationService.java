package com.fledge.fledgeserver.challenge.service;

import com.fledge.fledgeserver.canary.repository.CanaryProfileRepository;
import com.fledge.fledgeserver.challenge.dto.response.ChallengerParticipationPersonResponse;
import com.fledge.fledgeserver.challenge.repository.ChallengeRepository;
import com.fledge.fledgeserver.challenge.Enum.Frequency;
import com.fledge.fledgeserver.challenge.dto.response.TopParticipantResponse;
import com.fledge.fledgeserver.challenge.entity.ChallengeParticipation;
import com.fledge.fledgeserver.challenge.repository.ChallengeParticipationRepository;
import com.fledge.fledgeserver.challenge.repository.ChallengeProofRepository;
import com.fledge.fledgeserver.challenge.dto.response.ChallengeParticipationResponse;
import com.fledge.fledgeserver.challenge.entity.Challenge;
import com.fledge.fledgeserver.challenge.entity.ChallengeProof;
import com.fledge.fledgeserver.common.utils.SecurityUtils;
import com.fledge.fledgeserver.exception.CustomException;
import com.fledge.fledgeserver.exception.ErrorCode;
import com.fledge.fledgeserver.member.entity.Member;
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
    private final CanaryProfileRepository canaryProfileRepository;
    private final ChallengeRepository challengeRepository;

    @Transactional
    public ChallengeParticipationResponse participateInChallenge(Long memberId, Long challengeId, LocalDate startDate) {

        Member member = SecurityUtils.checkAndGetCurrentUser(memberId);

        if (!canaryProfileRepository.existsByMemberAndApprovalStatusIsTrue(member)){
            throw new CustomException(ErrorCode.CANARY_NOT_FOUND, "인증된 자립준비 청년이 아닙니다.");
        }

        if (participationRepository.existsByMemberIdAndChallengeId(memberId, challengeId)) {
            throw new CustomException(ErrorCode.CHALLENGE_PARTICIPATION_ALREADY_EXISTS);
        }

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
                    String memberNickname = (String) row[1];
                    Long participationCount = (Long) row[2];
                    Long successCount = (Long) row[3];
                    Double successRate = ((Number) row[4]).doubleValue() * 100;

                    List<String> topCategories = participationRepository.findTopCategoriesByMemberId(memberId);

                    return new TopParticipantResponse(memberId, memberNickname, participationCount, successCount, successRate, topCategories);
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

    @Transactional(readOnly = true)
    public List<ChallengerParticipationPersonResponse> getParticipantsByChallengeId(Long challengeId) {
        List<ChallengeParticipation> participations = participationRepository.findByChallengeId(challengeId);
        return participations.stream().map(participation -> {
            Member member = participation.getMember();
            long successCount = participationRepository.countSuccessByMemberId(member.getId());
            long totalParticipation = participationRepository.countByMemberId(member.getId());
            List<String> topCategories = participationRepository.findTopCategoriesByMemberId(member.getId());

            return new ChallengerParticipationPersonResponse(
                    member.getNickname(),
                    member.getProfile(),
                    successCount,
                    totalParticipation,
                    topCategories
            );
        }).collect(Collectors.toList());
    }
}

