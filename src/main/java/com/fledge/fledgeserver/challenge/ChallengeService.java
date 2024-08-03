package com.fledge.fledgeserver.challenge;

import com.fledge.fledgeserver.challenge.Enum.ChallengeCategory;
import com.fledge.fledgeserver.challenge.Enum.ChallengeType;
import com.fledge.fledgeserver.challenge.dto.ChallengeResponse;
import com.fledge.fledgeserver.challenge.entity.Challenge;
import com.fledge.fledgeserver.challenge.entity.OrganizationChallenge;
import com.fledge.fledgeserver.challenge.entity.PartnershipChallenge;
import com.fledge.fledgeserver.exception.CustomException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.fledge.fledgeserver.exception.ErrorCode.CHALLENGE_TYPE_INVALID;

@Service
public class ChallengeService {

    private final ChallengeRepository challengeRepository;

    public ChallengeService(ChallengeRepository challengeRepository) {
        this.challengeRepository = challengeRepository;
    }

    public Page<ChallengeResponse> getChallenges(int page, int size, String type, List<ChallengeCategory> categories) {
        Sort.Direction direction = Sort.Direction.DESC;
        String sortBy;

        if ("popular".equals(type)) {
            sortBy = "likeCount";
        } else if ("new".equals(type)) {
            sortBy = "registrationDate";
        } else {
            throw new CustomException(CHALLENGE_TYPE_INVALID, "유효하지 않은 type 값입니다.");
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
                challenge.getDescription(),
                (double) challenge.getSuccessCount() / challenge.getParticipantCount(),
                challenge.getParticipantCount()
        ));
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
                challenge.getDescription(),
                (double) challenge.getSuccessCount() / challenge.getParticipantCount(),
                challenge.getParticipantCount(),
                challenge instanceof PartnershipChallenge ? ((PartnershipChallenge) challenge).getSupportContent() : ((OrganizationChallenge) challenge).getSupportContent(),
                challenge instanceof PartnershipChallenge ? ((PartnershipChallenge) challenge).getStartDate() : ((OrganizationChallenge) challenge).getStartDate(),
                challenge instanceof PartnershipChallenge ? ((PartnershipChallenge) challenge).getEndDate() : ((OrganizationChallenge) challenge).getEndDate()
        ));
    }
}
