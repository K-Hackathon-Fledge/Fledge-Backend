package com.fledge.fledgeserver.support.controller;

import com.fledge.fledgeserver.common.util.MemberUtil;
import com.fledge.fledgeserver.response.ApiResponse;
import com.fledge.fledgeserver.support.dto.request.SupportCreateRequestDto;
import com.fledge.fledgeserver.support.dto.response.SupportDetailGetResponseDto;
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
    public ResponseEntity<ApiResponse<Object>> createSupport(Principal principal, @RequestBody SupportCreateRequestDto supportCreateRequestDto){
        Long memberId = MemberUtil.getMemberId(principal);
        supportService.createSupport(memberId, supportCreateRequestDto);
        return ApiResponse.success(CREATE_SUPPORT_SUCCESS);
    }

    @Operation(summary = "후원하기 게시글 상세 페이지 조회", description = "후원하기 게시글 상세 페이지 조회입니다.")
    @GetMapping("/{supportId}")
    public ResponseEntity<ApiResponse<SupportDetailGetResponseDto>> getSupport(@PathVariable Long supportId) {
        return ApiResponse.success(GET_SUPPORT_SUCCESS, supportService.getSupport(supportId));
    }

    // TODO :: 임박한 후원글 D-7까지 4개씩 최대 20개


    // TODO :: 후원글 목록


}
