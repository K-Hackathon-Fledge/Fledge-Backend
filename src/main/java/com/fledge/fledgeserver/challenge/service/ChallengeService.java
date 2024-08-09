package com.fledge.fledgeserver.challenge.service;

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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.fledge.fledgeserver.exception.ErrorCode.CHALLENGE_TYPE_INVALID;

@Service
@RequiredArgsConstructor
public class ChallengeService {

    private final ChallengeRepository challengeRepository;
    private final ChallengeParticipationRepository challengeParticipationRepository;

    public Page<ChallengeResponse> getChallenges(int page, int size, String type, List<ChallengeCategory> categories) {
        Sort.Direction direction = Sort.Direction.DESC;
        String sortBy;

        if ("popular".equals(type)) {
            sortBy = "likeCount";
        } else if ("new".equals(type)) {
            sortBy = "registrationDate";
        } else {
            throw new CustomException(CHALLENGE_TYPE_INVALID);
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        Page<Challenge> challenges;
        if (categories != null && !categories.isEmpty()) {
            challenges = challengeRepository.findByTypeAndCategoriesIn(ChallengeType.GENERAL, categories, pageable);
        } else {
            challenges = challengeRepository.findByType(ChallengeType.GENERAL, pageable);
        }

        return challenges.map(challenge -> new ChallengeResponse(
                challenge.getTitle(),
                challenge.getLikeCount(),
                challenge.getCategories(),
                challenge.getType().name(),
                challenge.getDescription(),
                (double) challenge.getSuccessCount() / challenge.getParticipantCount(),
                challenge.getSuccessCount(),
                challenge.getParticipantCount()
        ));
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
                (double) challenge.getSuccessCount() / challenge.getParticipantCount(),
                challenge.getSuccessCount(),
                challenge.getParticipantCount(),
                isParticipating
        );
    }

    public Page<ChallengeResponse> getPartnershipAndOrganizationChallenges(int page, int size, List<ChallengeCategory> categories) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "registrationDate"));

        Page<Challenge> challenges;
        if (categories != null && !categories.isEmpty()) {
            challenges = challengeRepository.findByTypeInAndCategoriesIn(List.of(ChallengeType.PARTNERSHIP, ChallengeType.ORGANIZATION), categories, pageable);
        } else {
            challenges = challengeRepository.findByTypeIn(List.of(ChallengeType.PARTNERSHIP, ChallengeType.ORGANIZATION), pageable);
        }

        return challenges.map(challenge -> new ChallengeResponse(
                challenge.getTitle(),
                challenge.getLikeCount(),
                challenge.getCategories(),
                challenge.getType().name(),
                challenge.getDescription(),
                (double) challenge.getSuccessCount() / challenge.getParticipantCount(),
                challenge.getSuccessCount(),
                challenge.getParticipantCount(),
                challenge instanceof PartnershipChallenge ? ((PartnershipChallenge) challenge).getSupportContent() : ((OrganizationChallenge) challenge).getSupportContent(),
                challenge instanceof PartnershipChallenge ? ((PartnershipChallenge) challenge).getStartDate() : ((OrganizationChallenge) challenge).getStartDate(),
                challenge instanceof PartnershipChallenge ? ((PartnershipChallenge) challenge).getEndDate() : ((OrganizationChallenge) challenge).getEndDate()
        ));
    }


}
