package com.fledge.fledgeserver.canary.controller;

import com.fledge.fledgeserver.auth.dto.OAuthUserImpl;
import com.fledge.fledgeserver.canary.dto.*;
import com.fledge.fledgeserver.canary.service.CanaryProfileService;
import com.fledge.fledgeserver.common.utils.SecurityUtils;
import com.fledge.fledgeserver.response.ApiResponse;
import com.fledge.fledgeserver.response.SuccessStatus;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.security.Principal;

import static com.fledge.fledgeserver.response.SuccessStatus.GET_SUPPORT_SUCCESS;

@Tag(name = "자립준비청년 API", description = "자립준비청년 관리 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/canary")
public class CanaryProfileController {

    private final CanaryProfileService canaryProfileService;

    @Operation(summary = "자립준비청년 인증 신청", description = "자립준비청년 인증을 신청합니다.")
    @PostMapping("/apply")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "프로필 신청 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "회원 정보를 찾을 수 없음"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "이미 신청한 유저")
    })
    public ResponseEntity<ApiResponse<Void>> applyForCanaryProfile(@Valid @RequestBody CanaryProfileRequest request) {
        canaryProfileService.createCanaryProfile(request);
        return ApiResponse.success(SuccessStatus.PROFILE_APPLICATION_SUCCESS);
    }

    @Operation(summary = "자립준비청년 승인 상태 조회", description = "사용자 ID로 연결된 자립준비청년의 승인 상태를 조회합니다. 0 : 신청 전, 1 : 신청 후, 2 : 승인됨 ")
    @GetMapping("/{userId}/status")
    public ResponseEntity<ApiResponse<Integer>> getApprovalStatus(
            @Parameter(description = "사용자 ID", required = true, example = "1") @PathVariable Long userId) {
        int status = canaryProfileService.getApprovalStatus(userId);
        return ApiResponse.success(SuccessStatus.PROFILE_RETRIEVAL_SUCCESS, status);
    }

    @Operation(summary = "자립준비청년 프로필 조회", description = "사용자 ID로 연결된 자립준비청년 프로필을 조회합니다.")
    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<CanaryProfileResponse>> getCanaryProfile(
            @Parameter(description = "사용자 ID", required = true, example = "1") @PathVariable Long userId) {
        CanaryProfileResponse response = canaryProfileService.getCanaryProfile(userId);
        return ApiResponse.success(SuccessStatus.PROFILE_RETRIEVAL_SUCCESS, response);
    }

    @Operation(summary = "자립준비청년 프로필 수정", description = "사용자 ID로 연결된 자립준비청년 프로필을 수정합니다.")
    @PutMapping("/{userId}")
    public ResponseEntity<ApiResponse<CanaryProfileResponse>> updateCanaryProfile(
            @Parameter(description = "사용자 ID", required = true, example = "1") @PathVariable Long userId,
            @Valid @RequestBody CanaryProfileUpdateRequest request) {
        CanaryProfileResponse response = canaryProfileService.updateCanaryProfile(userId, request);
        return ApiResponse.success(SuccessStatus.PROFILE_UPDATE_SUCCESS, response);
    }

    /**
     * 이하 API 후원하기 시에 필요
     */

    @Operation(summary = "자립준비청년 배송지 정보 조회", description = "자립준비청년 후원글 작성 시 배송지 정보를 불러올 수 있습니다.")
    @GetMapping("/delivery")
    public ResponseEntity<ApiResponse<CanaryGetDeliveryInfoResponse>> getCanaryDeliveryInfo() {
        CanaryGetDeliveryInfoResponse canaryGetDeliveryInfoResponse = canaryProfileService.getCanaryDeliveryInfo();
        return ApiResponse.success(SuccessStatus.DELIVERY_INFO_GET_SUCCESS, canaryGetDeliveryInfoResponse);
    }

    @Operation(summary = "후원하기 게시글 조회 시 자립준비청년 프로필 조회", description = "후원하기 게시글에서 자립준비청년 프로필을 조회합니다.")
    @GetMapping("/{memberId}/supports")
    public ResponseEntity<ApiResponse<CanaryProfileGetResponse>> getSupport(
            @PathVariable(value = "memberId") Long memberId
    ) {
        // TODO :: 자립준비청년이 완료한 챌린지 및 후원 인증 스토리 그리고 인증률도 함께 보여줘야함!
        CanaryProfileGetResponse canaryProfileGetResponse = canaryProfileService.getCanaryForSupport(memberId);
        return ApiResponse.success(GET_SUPPORT_SUCCESS, canaryProfileGetResponse);
    }
}