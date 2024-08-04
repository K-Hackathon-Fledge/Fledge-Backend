package com.fledge.fledgeserver.member.controller;

import com.fledge.fledgeserver.auth.dto.OAuthUserImpl;
import com.fledge.fledgeserver.member.dto.MemberNicknameUpdateRequest;
import com.fledge.fledgeserver.member.dto.MemberResponse;
import com.fledge.fledgeserver.member.service.MemberService;
import com.fledge.fledgeserver.response.ApiResponse;
import com.fledge.fledgeserver.response.SuccessStatus;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;

@Tag(name = "회원 관리 API", description = "회원 관리와 관련된 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/members")
public class MemberController {

    private final MemberService memberService;


    @Operation(summary = "현재 유저 정보 조회", description = "현재 인증된 유저의 정보를 조회합니다.")
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<MemberResponse>> memberInfo(
            @AuthenticationPrincipal OAuthUserImpl oAuth2User) {
        return ApiResponse.success(SuccessStatus.MEMBER_INFO_RETRIEVAL_SUCCESS, memberService.memberInfo(oAuth2User));
    }

    @Operation(summary = "회원 상세 정보 조회", description = "회원 ID로 회원의 상세 정보를 조회합니다.")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<MemberResponse>> getMemberDetails(
            @Parameter(description = "회원 ID", required = true, example = "1") @PathVariable Long id) {
        MemberResponse memberResponse = memberService.getMemberDetails(id);
        return ApiResponse.success(SuccessStatus.MEMBER_DETAILS_RETRIEVAL_SUCCESS, memberResponse);
    }

    @Operation(summary = "회원 닉네임 수정", description = "회원 ID로 회원의 닉네임을 수정합니다.")
    @PutMapping("/{id}/nickname")
    public ResponseEntity<ApiResponse<MemberResponse>> updateNickname(
            @Parameter(description = "회원 ID", required = true, example = "1") @PathVariable Long id,
            @Parameter(description = "닉네임 수정 요청", required = true) @RequestBody MemberNicknameUpdateRequest request,
            @AuthenticationPrincipal OAuthUserImpl oAuth2User) {
        MemberResponse memberResponse = memberService.updateNickname(id, request.getNickname());
        return ApiResponse.success(SuccessStatus.MEMBER_NICKNAME_UPDATE_SUCCESS, memberResponse);
    }
}
