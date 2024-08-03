package com.fledge.fledgeserver.challenge;

import com.fledge.fledgeserver.challenge.Enum.ChallengeCategory;
import com.fledge.fledgeserver.challenge.dto.ChallengeResponse;
import com.fledge.fledgeserver.response.ApiResponse;
import com.fledge.fledgeserver.response.SuccessStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "챌린지 관련 API", description = "챌린지 등록 및 조회, 참여와 관련된 API")
@RestController
@RequestMapping("/api/v3/challenges")
public class ChallengeController {

    private final ChallengeService challengeService;

    public ChallengeController(ChallengeService challengeService) {
        this.challengeService = challengeService;
    }

    @Operation(summary = "일반 챌린지 조회", description = "좋아요 수 또는 등록일로 정렬된 챌린지 리스트를 조회합니다. 일반 챌린지만 포함됩니다.")
    @GetMapping
    public ResponseEntity<ApiResponse<Page<ChallengeResponse>>> getChallenges(
            @Parameter(example = "0")
            @RequestParam int page,
            @Parameter(example = "8")
            @RequestParam int size,
            @Parameter(description = "조회 타입: 'popular' 또는 'new'", example = "popular")
            @RequestParam String type,
            @RequestParam(required = false) List<ChallengeCategory> categories) {

        Page<ChallengeResponse> challengeResponses = challengeService.getChallenges(page, size, type, categories);

        return ApiResponse.success(SuccessStatus.CHALLENGE_RETRIEVAL_SUCCESS, challengeResponses);
    }

}



