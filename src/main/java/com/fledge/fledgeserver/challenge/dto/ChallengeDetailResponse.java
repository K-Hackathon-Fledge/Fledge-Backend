package com.fledge.fledgeserver.challenge.dto;

import com.fledge.fledgeserver.challenge.Enum.ChallengeCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Schema(description = "챌린지 상세 정보 응답 DTO")
public class ChallengeDetailResponse extends ChallengeResponse {

    @Schema(description = "사용자 참여 여부", example = "true")
    private boolean isParticipating;

    public ChallengeDetailResponse(String title, int likeCount, List<ChallengeCategory> categories, String type,
                                   String description, double successRate, int successCount, int participantCount, boolean isParticipating) {
        super(title, likeCount, categories, type, description, successRate, successCount, participantCount, null, null, null);
        this.isParticipating = isParticipating;
    }
}
