package com.fledge.fledgeserver.challenge;

import com.fledge.fledgeserver.challenge.Enum.ChallengeCategory;
import com.fledge.fledgeserver.challenge.Enum.ChallengeType;
import com.fledge.fledgeserver.challenge.entity.Challenge;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChallengeRepository extends JpaRepository<Challenge, Long> {
    Page<Challenge> findByType(ChallengeType type, Pageable pageable);

    Page<Challenge> findByTypeAndCategoriesIn(ChallengeType general, List<ChallengeCategory> categories, Pageable pageable);

    Page<Challenge> findByTypeIn(List<ChallengeType> types, Pageable pageable);

    Page<Challenge> findByTypeInAndCategoriesIn(List<ChallengeType> types, List<ChallengeCategory> categories, Pageable pageable);
}
