package com.fledge.fledgeserver.challenge.repository;

import com.fledge.fledgeserver.challenge.entity.ChallengeParticipation;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChallengeParticipationRepository extends JpaRepository<ChallengeParticipation, Long> {

    @Query("SELECT p.member.id AS memberId, p.member.nickname AS nickname, COUNT(p) AS participationCount, SUM(CASE WHEN p.isSuccess = true THEN 1 ELSE 0 END) AS successCount, " +
            "(SUM(CASE WHEN p.isSuccess = true THEN 1 ELSE 0 END) * 1.0 / COUNT(p)) AS successRate " +
            "FROM ChallengeParticipation p " +
            "GROUP BY p.member.id " +
            "ORDER BY (SUM(CASE WHEN p.isSuccess = true THEN 1 ELSE 0 END) * 1.0 / COUNT(p)) DESC")
    List<Object[]> findTopParticipants(Pageable pageable);

    @Query(value = "SELECT cc.category " +
            "FROM challenge_participation p " +
            "JOIN challenge c ON p.challenge_id = c.id " +
            "JOIN challenge_categories cc ON c.id = cc.challenge_id " +
            "WHERE p.member_id = :memberId " +
            "GROUP BY cc.category " +
            "ORDER BY COUNT(p.id) DESC " +
            "LIMIT 3", nativeQuery = true)
    List<String> findTopCategoriesByMemberId(@Param("memberId") Long memberId);
}
