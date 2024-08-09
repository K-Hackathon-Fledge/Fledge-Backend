package com.fledge.fledgeserver.challenge.service;

import com.fledge.fledgeserver.canary.repository.CanaryProfileRepository;
import com.fledge.fledgeserver.challenge.dto.MyChallengeProofResponse;
import com.fledge.fledgeserver.challenge.dto.ProofDetail;
import com.fledge.fledgeserver.challenge.entity.ChallengeParticipation;
import com.fledge.fledgeserver.challenge.repository.ChallengeParticipationRepository;
import com.fledge.fledgeserver.challenge.repository.ChallengeProofRepository;
import com.fledge.fledgeserver.challenge.dto.ChallengeProofResponse;
import com.fledge.fledgeserver.challenge.entity.ChallengeProof;
import com.fledge.fledgeserver.common.utils.SecurityUtils;
import com.fledge.fledgeserver.exception.CustomException;
import com.fledge.fledgeserver.exception.ErrorCode;
import com.fledge.fledgeserver.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChallengeProofService {

    private final ChallengeProofRepository proofRepository;
    private final ChallengeParticipationRepository participationRepository;
    private final CanaryProfileRepository canaryProfileRepository;

    @Transactional
    public ChallengeProofResponse uploadProof(Long participationId, LocalDate proofDate, String proofImageUrl) {
        Member member = SecurityUtils.getCurrentMember();
        if (!canaryProfileRepository.existsByMemberAndApprovalStatusIsTrue(member)){
            throw new CustomException(ErrorCode.CANARY_NOT_FOUND, "인증된 자립준비 청년이 아닙니다.");
        }
        ChallengeProof proof = proofRepository.findByParticipationIdAndProofDate(participationId, proofDate)
                .orElseThrow(() -> new CustomException(ErrorCode.CHALLENGE_PROOF_NOT_FOUND));

        if (proof.isProofed()) {
            throw new CustomException(ErrorCode.CHALLENGE_PROOF_ALREADY_SUBMITTED);
        }

        proof.markAsProofed();
        proof.updateProofImageUrl(proofImageUrl);
        proofRepository.save(proof);

        return new ChallengeProofResponse(proof);
    }

    @Transactional(readOnly = true)
    public MyChallengeProofResponse getMyProofsByChallengeId(Long challengeId) {
        Member member = SecurityUtils.getCurrentMember();
        if (!canaryProfileRepository.existsByMemberAndApprovalStatusIsTrue(member)){
            throw new CustomException(ErrorCode.CANARY_NOT_FOUND, "인증된 자립준비 청년이 아닙니다.");
        }

        ChallengeParticipation participation = participationRepository.findByMemberIdAndChallengeId(member.getId(), challengeId)
                .orElseThrow(() -> new CustomException(ErrorCode.CHALLENGE_PARTICIPATION_NOT_FOUND));

        List<ProofDetail> proofDetails = proofRepository.findByParticipationId(participation.getId()).stream()
                .map(proof -> new ProofDetail(
                        proof.isProofed(),
                        proof.getProofImageUrl(),
                        proof.getProofDescription()
                )).collect(Collectors.toList());

        int totalProofs = proofDetails.size();

        return new MyChallengeProofResponse(totalProofs, proofDetails);
    }
}

