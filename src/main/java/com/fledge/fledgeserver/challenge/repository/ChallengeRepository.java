package com.fledge.fledgeserver.challenge.repository;

import com.fledge.fledgeserver.challenge.Enum.ChallengeCategory;
import com.fledge.fledgeserver.challenge.Enum.ChallengeType;
import com.fledge.fledgeserver.challenge.entity.Challenge;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChallengeRepository extends JpaRepository<Challenge, Long> {
    Page<Challenge> findByType(ChallengeType type, Pageable pageable);

    Page<Challenge> findByTypeAndCategoriesIn(ChallengeType general, List<ChallengeCategory> categories, Pageable pageable);

    Page<Challenge> findByTypeIn(List<ChallengeType> types, Pageable pageable);

    Page<Challenge> findByTypeInAndCategoriesIn(List<ChallengeType> types, List<ChallengeCategory> categories, Pageable pageable);

    List<Challenge> findTop16ByCategoriesInAndIdNot(List<ChallengeCategory> categories, Long challengeId);

    @Query("SELECT c FROM Challenge c WHERE c.id <> :challengeId ORDER BY function('RAND')")
    List<Challenge> findRandomChallengesExcludingId(@Param("challengeId") Long challengeId, Pageable pageable);

}
