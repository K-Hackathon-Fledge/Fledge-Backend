package com.fledge.fledgeserver.challenge.service;

import com.fledge.fledgeserver.challenge.ChallengeConstants;
import com.fledge.fledgeserver.challenge.dto.response.ChallengeDetailResponse;
import com.fledge.fledgeserver.challenge.repository.ChallengeParticipationRepository;
import com.fledge.fledgeserver.challenge.repository.ChallengeRepository;
import com.fledge.fledgeserver.challenge.Enum.ChallengeCategory;
import com.fledge.fledgeserver.challenge.Enum.ChallengeType;
import com.fledge.fledgeserver.challenge.dto.response.ChallengeResponse;
import com.fledge.fledgeserver.challenge.entity.Challenge;
import com.fledge.fledgeserver.challenge.entity.OrganizationChallenge;
import com.fledge.fledgeserver.challenge.entity.PartnershipChallenge;
import com.fledge.fledgeserver.common.utils.SecurityUtils;
import com.fledge.fledgeserver.exception.CustomException;
import com.fledge.fledgeserver.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.fledge.fledgeserver.exception.ErrorCode.CHALLENGE_TYPE_INVALID;

@Service
@RequiredArgsConstructor
public class ChallengeService {

    private final ChallengeRepository challengeRepository;
    private final ChallengeParticipationRepository challengeParticipationRepository;
    private final DecimalFormat df = new DecimalFormat("#.0");

    public Page<ChallengeResponse> getChallenges(int page, int size, String type, List<ChallengeCategory> categories) {
        String sortBy;

        if (ChallengeConstants.POPULAR_TYPE.equals(type)) {
            sortBy = ChallengeConstants.SORT_BY_LIKE_COUNT;
        } else if (ChallengeConstants.NEW_TYPE.equals(type)) {
            sortBy = ChallengeConstants.SORT_BY_REGISTRATION_DATE;
        } else {
            throw new CustomException(ErrorCode.CHALLENGE_TYPE_INVALID);
        }

        return getChallengesByTypeAndCategories(page, size, List.of(ChallengeType.GENERAL), categories, sortBy, false);
    }

    public ChallengeDetailResponse getChallengeById(Long challengeId) {
        Challenge challenge = challengeRepository.findById(challengeId)
                .orElseThrow(() -> new CustomException(ErrorCode.CHALLENGE_NOT_FOUND));

        boolean isParticipating = SecurityUtils.isAuthenticated() && challengeParticipationRepository.existsByMemberIdAndChallengeId(SecurityUtils.getCurrentUserId(), challengeId);

        return new ChallengeDetailResponse(
                challenge.getTitle(),
                challenge.getLikeCount(),
                challenge.getCategories(),
                challenge.getType().name(),
                challenge.getDescription(),
                calculateSuccessRate(challenge),
                challenge.getSuccessCount(),
                challenge.getParticipantCount(),
                isParticipating
        );
    }

    public Page<ChallengeResponse> getPartnershipAndOrganizationChallenges(int page, int size, List<ChallengeCategory> categories) {
        return getChallengesByTypeAndCategories(page, size, List.of(ChallengeType.PARTNERSHIP, ChallengeType.ORGANIZATION), categories, ChallengeConstants.SORT_BY_REGISTRATION_DATE, true);
    }

    public List<ChallengeResponse> exploreOtherChallenges(Long challengeId) {
        Challenge challenge = challengeRepository.findById(challengeId)
                .orElseThrow(() -> new CustomException(ErrorCode.CHALLENGE_NOT_FOUND));

        List<ChallengeCategory> categories = challenge.getCategories();

        List<Challenge> sameCategoryChallenges = challengeRepository.findTop16ByCategoriesInAndIdNot(categories, challengeId);

        int remainingSize = 16 - sameCategoryChallenges.size();
        if (remainingSize > 0) {
            Pageable pageable = PageRequest.of(0, remainingSize);
            List<Challenge> randomChallenges = challengeRepository.findRandomChallengesExcludingId(challengeId, pageable);

            Collections.shuffle(randomChallenges);

            sameCategoryChallenges.addAll(randomChallenges);
        }

        return sameCategoryChallenges.stream()
                .map(this::createChallengeResponse)
                .collect(Collectors.toList());
    }

    private Page<ChallengeResponse> getChallengesByTypeAndCategories(int page, int size, List<ChallengeType> types, List<ChallengeCategory> categories, String sortBy, boolean includeSupport) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, sortBy));

        Page<Challenge> challenges;
        if (categories != null && !categories.isEmpty()) {
            challenges = challengeRepository.findByTypeInAndCategoriesIn(types, categories, pageable);
        } else {
            challenges = challengeRepository.findByTypeIn(types, pageable);
        }

        return challenges.map(challenge -> includeSupport ? createChallengeResponseWithSupport(challenge) : createChallengeResponse(challenge));
    }

    private ChallengeResponse createChallengeResponse(Challenge challenge) {
        return new ChallengeResponse(
                challenge.getTitle(),
                challenge.getLikeCount(),
                challenge.getCategories(),
                challenge.getType().name(),
                challenge.getDescription(),
                calculateSuccessRate(challenge),
                challenge.getSuccessCount(),
                challenge.getParticipantCount(),
                null, null, null
        );
    }

    private ChallengeResponse createChallengeResponseWithSupport(Challenge challenge) {
        return new ChallengeResponse(
                challenge.getTitle(),
                challenge.getLikeCount(),
                challenge.getCategories(),
                challenge.getType().name(),
                challenge.getDescription(),
                calculateSuccessRate(challenge),
                challenge.getSuccessCount(),
                challenge.getParticipantCount(),
                challenge instanceof PartnershipChallenge ? ((PartnershipChallenge) challenge).getSupportContent() : ((OrganizationChallenge) challenge).getSupportContent(),
                challenge instanceof PartnershipChallenge ? ((PartnershipChallenge) challenge).getStartDate() : ((OrganizationChallenge) challenge).getStartDate(),
                challenge instanceof PartnershipChallenge ? ((PartnershipChallenge) challenge).getEndDate() : ((OrganizationChallenge) challenge).getEndDate()
        );
    }

    private double calculateSuccessRate(Challenge challenge) {
        return Double.parseDouble(df.format((double) challenge.getSuccessCount() / challenge.getParticipantCount()));
    }
}

