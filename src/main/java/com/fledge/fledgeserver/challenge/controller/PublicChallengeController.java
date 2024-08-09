package com.fledge.fledgeserver.challenge.controller;

import com.fledge.fledgeserver.challenge.Enum.ChallengeCategory;
import com.fledge.fledgeserver.challenge.dto.*;
import com.fledge.fledgeserver.challenge.service.ChallengeParticipationService;
import com.fledge.fledgeserver.challenge.service.ChallengeService;
import com.fledge.fledgeserver.response.ApiResponse;
import com.fledge.fledgeserver.response.SuccessStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "챌린지 관련 API (Public)", description = "챌린지 등록 및 조회, 참여와 관련된 API (Public)")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/public/challenges")
public class PublicChallengeController {

    private final ChallengeService challengeService;
    private final ChallengeParticipationService participationService;

    @Operation(summary = "일반 챌린지 리스트 조회", description = "좋아요 수 또는 등록일로 정렬된 챌린지 리스트를 조회합니다. 일반 챌린지만 포함됩니다.")
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

    @Operation(summary = "일반 챌린지 상세 조회", description = "일반 챌린지 ID를 통해 특정 챌린지의 상세 정보를 조회합니다.")
    @GetMapping("/{challengeId}")
    public ResponseEntity<ApiResponse<ChallengeResponse>> getChallengeById(
            @Parameter(description = "챌린지 ID", example = "1")
            @PathVariable Long challengeId) {

        ChallengeResponse challengeDetail = challengeService.getChallengeById(challengeId);
        return ApiResponse.success(SuccessStatus.CHALLENGE_RETRIEVAL_SUCCESS, challengeDetail);
    }

    @Operation(summary = "연계챌린지 조회", description = "PARTNERSHIP 및 ORGANIZATION 타입의 챌린지를 조회합니다.")
    @GetMapping("/partnership-and-organization")
    public ResponseEntity<ApiResponse<Page<ChallengeResponse>>> getPartnershipAndOrganizationChallenges(
            @Parameter(example = "0")
            @RequestParam int page,
            @Parameter(example = "8")
            @RequestParam int size,
            @RequestParam(required = false) List<ChallengeCategory> categories) {

        Page<ChallengeResponse> challengeResponses = challengeService.getPartnershipAndOrganizationChallenges(page, size, categories);

        return ApiResponse.success(SuccessStatus.CHALLENGE_RETRIEVAL_SUCCESS, challengeResponses);
    }

    @Operation(summary = "가장 성공률이 높은 참여자 목록 조회", description = "가장 성공률이 높은 참여자 상위 20명을 조회합니다.")
    @GetMapping("/top-participants")
    public ResponseEntity<ApiResponse<List<TopParticipantResponse>>> getTopParticipants() {
        List<TopParticipantResponse> topParticipants = participationService.getTopParticipants(20);
        return ApiResponse.success(SuccessStatus.CHALLENGE_RETRIEVAL_SUCCESS, topParticipants);
    }

    @Operation(summary = "챌린지 참여자 목록 조회", description = "특정 챌린지에 참여 중인 사용자들의 목록을 조회합니다.")
    @GetMapping("/{challengeId}/participants")
    public ResponseEntity<ApiResponse<List<ChallengerParticipationPersonResponse>>> getChallengeParticipants(@PathVariable Long challengeId) {
        List<ChallengerParticipationPersonResponse> participants = participationService.getParticipantsByChallengeId(challengeId);
        return ApiResponse.success(SuccessStatus.CHALLENGE_PARTICIPANTS_RETRIEVED_SUCCESS, participants);
    }


}



