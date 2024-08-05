package com.fledge.fledgeserver.challenge.controller;

import com.fledge.fledgeserver.challenge.Enum.ChallengeCategory;
import com.fledge.fledgeserver.challenge.dto.*;
import com.fledge.fledgeserver.challenge.service.ChallengeParticipationService;
import com.fledge.fledgeserver.challenge.service.ChallengeProofService;
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

@Tag(name = "챌린지 관련 API", description = "챌린지 등록 및 조회, 참여와 관련된 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/challenges")
public class ChallengeController {

    private final ChallengeParticipationService participationService;
    private final ChallengeProofService proofService;

    @Operation(summary = "챌린지 참여", description = "사용자가 특정 챌린지에 참여합니다.")
    @PostMapping
    public ResponseEntity<ApiResponse<ChallengeParticipationResponse>> participateInChallenge(@RequestBody ChallengeParticipationRequest request) {
        ChallengeParticipationResponse response = participationService.participateInChallenge(request.getMemberId(), request.getChallengeId(), request.getStartDate());
        return ApiResponse.success(SuccessStatus.CHALLENGE_PARTICIPATION_SUCCESS, response);
    }

    @Operation(summary = "챌린지 인증 업로드", description = "사용자가 특정 챌린지 참여에 대해 인증을 업로드합니다.")
    @PostMapping("/{participationId}/proofs")
    public ResponseEntity<ApiResponse<ChallengeProofResponse>> uploadProof(
            @PathVariable Long participationId,
            @RequestBody ChallengeProofRequest request) {
        ChallengeProofResponse response = proofService.uploadProof(participationId, request.getProofDate(), request.getProofImageUrl());
        return ApiResponse.success(SuccessStatus.CHALLENGE_PROOF_UPLOAD_SUCCESS, response);
    }

}



