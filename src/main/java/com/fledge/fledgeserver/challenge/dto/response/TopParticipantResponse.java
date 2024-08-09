package com.fledge.fledgeserver.challenge.dto.response;

import com.fledge.fledgeserver.challenge.Enum.ChallengeCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;


@AllArgsConstructor
@Getter
@Schema(description = "최고 참가자 응답 DTO")
public class TopParticipantResponse {

    @Schema(description = "회원 ID", example = "1")
    private Long memberId;

    @Schema(description = "회원 이름", example = "닉네임")
    private String memberName;

    @Schema(description = "참여 횟수", example = "10")
    private Long participationCount;

    @Schema(description = "성공한 참여 횟수", example = "8")
    private Long successCount;

    @Schema(description = "성공률(백분율)", example = "80.0")
    private Double successRate;

    @Schema(description = "회원이 자주 성공한 챌린지 카테고리 목록 (최대 3개)")
    private List<String> topCategories;
}
