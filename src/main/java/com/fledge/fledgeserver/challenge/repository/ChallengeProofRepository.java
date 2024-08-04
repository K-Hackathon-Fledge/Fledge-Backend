package com.fledge.fledgeserver.challenge.repository;

import com.fledge.fledgeserver.challenge.entity.ChallengeProof;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ChallengeProofRepository extends JpaRepository<ChallengeProof, Long> {
    Optional<ChallengeProof> findByParticipationIdAndProofDate(Long participationId, LocalDate proofDate);
    List<ChallengeProof> findByParticipationId(Long participationId);
}

