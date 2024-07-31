package com.fledge.fledgeserver.support.controller;

import com.fledge.fledgeserver.common.utils.SecurityUtils;
import com.fledge.fledgeserver.response.ApiResponse;
import com.fledge.fledgeserver.response.SuccessStatus;
import com.fledge.fledgeserver.support.dto.request.SupportCreateRequestDto;
import com.fledge.fledgeserver.support.dto.request.SupportUpdateRequestDto;
import com.fledge.fledgeserver.support.dto.response.SupportGetForUpdateResponseDto;
import com.fledge.fledgeserver.support.dto.response.SupportGetResponseDto;
import com.fledge.fledgeserver.support.service.SupportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

import static com.fledge.fledgeserver.response.SuccessStatus.CREATE_SUPPORT_SUCCESS;
import static com.fledge.fledgeserver.response.SuccessStatus.GET_SUPPORT_SUCCESS;

@Tag(name = "후원하기 API", description = "후원하기와 관련된 API")
@RestController
@RequestMapping("/api/v1/supports")
@RequiredArgsConstructor
public class SupportController {
    private final SupportService supportService;

    @Operation(summary = "후원하기 게시글 등록", description = "후원하기 게시글을 등록합니다.(자립 준비 청소년만)")
    @PostMapping
    public ResponseEntity<ApiResponse<Object>> createSupport(
            Principal principal,
            @RequestBody SupportCreateRequestDto supportCreateRequestDto
    ) {
        Long memberId = SecurityUtils.getCurrentUserId(principal);
        supportService.createSupport(memberId, supportCreateRequestDto);
        return ApiResponse.success(CREATE_SUPPORT_SUCCESS);
    }

    @Operation(summary = "후원하기 게시글 조회", description = "후원하기 게시글을 조회합니다.(모든 회원 가능)")
    @GetMapping("/{supportId}")
    public ResponseEntity<ApiResponse<SupportGetResponseDto>> getSupport(
            @PathVariable(value = "supportId") Long supportId
    ) {
        // TODO :: 후원하기(후원자) & 후원 인증 관련 로직 추가
        return ApiResponse.success(GET_SUPPORT_SUCCESS, supportService.getSupport(supportId));
    }

    @Operation(summary = "후원하기 게시글 수정 시 기존 데이터 조회", description = "후원하기 게시글의 기존 데이터를 반환합니다.")
    @GetMapping("/{supportId}/update")
    public ResponseEntity<ApiResponse<SupportGetForUpdateResponseDto>> getSupportForUpdate(
            @PathVariable(value = "supportId") Long supportId,
            Principal principal
    ) {
        Long memberId = SecurityUtils.getCurrentUserId(principal);
        return ApiResponse.success(
                SuccessStatus.GET_SUPPORT_FOR_UPDATE_SUCCESS,
                supportService.getSupportForUpdate(memberId, supportId)
        );
    }

    @Operation(summary = "후원하기 게시글 수정", description = "후원하기 게시글을 수정합니다.")
    @PutMapping("/{supportId}")
    public ResponseEntity<ApiResponse<SupportGetResponseDto>> updateSupport(
            Principal principal,
            @PathVariable(value = "supportId") Long supportId,
            @RequestBody SupportUpdateRequestDto supportUpdateRequestDto
    ) {
        Long memberId = SecurityUtils.getCurrentUserId(principal);
        supportService.updateSupport(memberId, supportId, supportUpdateRequestDto);
        return ApiResponse.success(SuccessStatus.UPDATE_SUPPORT_SUCCESS);
    }


    // TODO :: 후원하기 게시글 삭제 API
}
