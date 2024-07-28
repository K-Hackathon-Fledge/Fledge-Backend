package com.fledge.fledgeserver.canary.controller;

import com.fledge.fledgeserver.canary.dto.CanaryProfileRequest;
import com.fledge.fledgeserver.canary.dto.CanaryProfileResponse;
import com.fledge.fledgeserver.canary.dto.CanaryProfileUpdateRequest;
import com.fledge.fledgeserver.canary.service.CanaryProfileService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "자립준비청년 API", description = "자립준비청년 관리 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/canary")
public class CanaryProfileController {

    private final CanaryProfileService canaryProfileService;

    @Operation(summary = "자립준비청년 인증 신청", description = "자립준비청년 인증을 신청합니다.")
    @PostMapping("/apply")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "프로필 신청 성공"),
            @ApiResponse(responseCode = "404", description = "회원 정보를 찾을 수 없음"),
            @ApiResponse(responseCode = "409", description = "이미 신청한 유저")
    })
    public ResponseEntity<Void> applyForCanaryProfile(@Valid @RequestBody CanaryProfileRequest request) {
        canaryProfileService.createCanaryProfile(request);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "자립준비청년 프로필 조회", description = "사용자 ID로 연결된 자립준비청년 프로필을 조회합니다.")
    @GetMapping("/{userId}")
    public ResponseEntity<CanaryProfileResponse> getCanaryProfile(
            @Parameter(description = "사용자 ID", required = true, example = "1") @PathVariable Long userId) {
        CanaryProfileResponse response = canaryProfileService.getCanaryProfile(userId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "자립준비청년 프로필 수정", description = "사용자 ID로 연결된 자립준비청년 프로필을 수정합니다.")
    @PutMapping("/{userId}")
    public ResponseEntity<Void> updateCanaryProfile(
            @Parameter(description = "사용자 ID", required = true, example = "1") @PathVariable Long userId,
            @Valid @RequestBody CanaryProfileUpdateRequest request) {
        canaryProfileService.updateCanaryProfile(userId, request);
        return ResponseEntity.ok().build();
    }
}
