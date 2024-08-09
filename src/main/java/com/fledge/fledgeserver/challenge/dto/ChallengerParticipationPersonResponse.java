package com.fledge.fledgeserver.challenge.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "챌린지 참여자 정보 응답 DTO")
public class ChallengerParticipationPersonResponse {

    @Schema(description = "참여자 닉네임", example = "user123")
    private String nickname;

    @Schema(description = "참여자 프로필 이미지 URL", example = "https://example.com/profile.jpg")
    private String profileImageUrl;

    @Schema(description = "성공한 챌린지 수", example = "5")
    private long successCount;

    @Schema(description = "전체 참여 챌린지 수", example = "10")
    private long totalParticipation;

    @Schema(description = "자주 참여하는 챌린지 카테고리 목록", example = "[\"SELF_DEVELOPMENT\", \"FITNESS\"]")
    private List<String> topCategories;
}
