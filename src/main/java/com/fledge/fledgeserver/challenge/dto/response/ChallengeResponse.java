package com.fledge.fledgeserver.challenge.dto.response;

import com.fledge.fledgeserver.challenge.Enum.ChallengeCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Schema(description = "챌린지 응답 DTO")
public class ChallengeResponse {

    @Schema(description = "챌린지 제목", example = "챌린지 타이틀")
    private final String title;

    @Schema(description = "좋아요 수", example = "100")
    private final int likeCount;

    @Schema(description = "카테고리 목록")
    private final List<ChallengeCategory> categories;

    @Schema(description = "챌린지 유형", example = "SELF_DEVELOPMENT")
    private final String type;

    @Schema(description = "챌린지 설명", example = "챌린지 설명")
    private final String description;

    @Schema(description = "성공률(백분율)", example = "85.5")
    private final double successRate;

    @Schema(description = "성공한 참여자 수", example = "50")
    private final int successCount;

    @Schema(description = "참여자 수", example = "50")
    private final int participantCount;

    @Schema(description = "지원 내용", example = "지원 콘텐츠 설명")
    private String supportContent;

    @Schema(description = "시작 날짜", example = "2023-01-01")
    private LocalDate startDate;

    @Schema(description = "종료 날짜", example = "2023-02-01")
    private LocalDate endDate;
}