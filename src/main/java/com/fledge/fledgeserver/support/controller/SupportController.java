package com.fledge.fledgeserver.support.controller;

import com.fledge.fledgeserver.common.utils.SecurityUtils;
import com.fledge.fledgeserver.response.ApiResponse;
import com.fledge.fledgeserver.response.SuccessStatus;
import com.fledge.fledgeserver.support.dto.request.PostUpdateRequest;
import com.fledge.fledgeserver.support.dto.request.RecordCreateRequest;
import com.fledge.fledgeserver.support.dto.request.PostCreateRequest;
import com.fledge.fledgeserver.support.dto.response.*;
import com.fledge.fledgeserver.support.service.SupportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

import static com.fledge.fledgeserver.response.SuccessStatus.*;

@Tag(name = "후원하기 API", description = "후원하기와 관련된 API")
@RestController
@RequestMapping("/api/v1/supports")
@RequiredArgsConstructor
public class SupportController {
    private final SupportService supportService;

    @Operation(summary = "후원하기 게시글 등록",
            description = "후원하기 게시글을 등록합니다.(자립 준비 청소년만)")
    @PostMapping
    public ResponseEntity<ApiResponse<Object>> createSupport(
            Principal principal,
            @RequestBody PostCreateRequest postCreateRequest
    ) {
        Long memberId = SecurityUtils.getCurrentUserId(principal);
        supportService.createSupport(memberId, postCreateRequest);
        return ApiResponse.success(CREATE_SUPPORT_SUCCESS);
    }

    @Operation(summary = "후원하기 게시글 조회",
            description = "후원하기 게시글을 조회합니다.(모든 회원 가능)")
    @GetMapping("/{supportId}")
    public ResponseEntity<ApiResponse<PostGetResponse>> getSupport(
            @PathVariable(value = "supportId") Long supportId
    ) {
        // TODO :: 후원 인증 관련 로직 추가
        // TODO :: 후원 게시글 둘러보기를 후원하기 상세 페이지에서도 봐야함
        return ApiResponse.success(GET_SUPPORT_SUCCESS, supportService.getSupport(supportId));
    }

    @Operation(summary = "후원 물품 금액 후원하기",
            description = "후원하기 게시글에서 후원 물품 금액 조회합니다.(모든 회원 가능)")
    @PostMapping("/{supportId}/record")
    public ResponseEntity<ApiResponse<Object>> createSupportRecord(
            @PathVariable(value = "supportId") Long supportId,
            @RequestBody RecordCreateRequest donationRequestDto,
            Principal principal
    ) {
        Long memberId = SecurityUtils.getCurrentUserId(principal);
        supportService.createSupportRecord(supportId, donationRequestDto, memberId);
        return ApiResponse.success(CREATE_DONATE_SUCCESS);
    }

    @Operation(summary = "후원 진행률",
            description = "후원하기 게시글 및 후원하기 시에 후원 진행률 반환")
    @GetMapping("/{supportId}/progress")
    public ResponseEntity<ApiResponse<RecordProgressGetResponse>> getSupportProgress(
            @PathVariable(value = "supportId") Long supportId
    ) {
        return ApiResponse.success(GET_SUPPORT_PROGRESS_SUCCESS, supportService.getSupportProgress(supportId));
    }

    @Operation(summary = "후원하기 게시글 수정 시 기존 데이터 조회",
            description = "후원하기 게시글의 기존 데이터를 반환합니다.\nStatus가 PENDING이면 공통 필드 수정 가능, 그 외 수정 불가")
    @GetMapping("/{supportId}/update")
    public ResponseEntity<ApiResponse<PostGetForUpdateResponse>> getSupportForUpdate(
            @PathVariable(value = "supportId") Long supportId,
            Principal principal
    ) {
        Long memberId = SecurityUtils.getCurrentUserId(principal);
        return ApiResponse.success(
                GET_SUPPORT_FOR_UPDATE_SUCCESS, supportService.getSupportForUpdate(supportId, memberId)
        );
    }

    @Operation(summary = "후원하기 게시글 수정", description = "(이미지 업데이트 안됨)후원하기 게시글을 수정합니다.")
    @PutMapping("/{supportId}")
    public ResponseEntity<ApiResponse<PostGetResponse>> updateSupportPost(
            Principal principal,
            @PathVariable(value = "supportId") Long supportId,
            @RequestBody PostUpdateRequest postUpdateRequest
    ) {
        Long memberId = SecurityUtils.getCurrentUserId(principal);
        supportService.updateSupportPost(memberId, supportId, postUpdateRequest);
        return ApiResponse.success(SuccessStatus.UPDATE_SUPPORT_SUCCESS);
    }

    // TODO :: 후원하기 게시글 삭제 API


    @Operation(summary = "후원하기 게시글 리스트 페이징",
            description = "q:검색어(제목,내용), category:카테고리 그리고 상태:status('ing'/'end') 기준으로 조회.")
    @GetMapping("/paging")
    public ResponseEntity<ApiResponse<PostTotalPagingResponse>> pagingSupportPost(
            @RequestParam(defaultValue = "1") int page, // 현재 페이지
//            @RequestParam(defaultValue = "10") int limit //무조건 9개
            @RequestParam(defaultValue = "") String q,
            @RequestParam(defaultValue = "") List<String> category, // 카테고리
            @RequestParam(defaultValue = "") String status
    ) {
        // 응답에 이미지 포함 시키기
        return ApiResponse.success(GET_SUPPORT_POST_PAGING_SUCCESS, supportService.pagingSupportPost(page-1, q, category, status));
    }

    @Operation(summary = "마감 임박한 후원하기 게시글",
            description = "4개씩 D-7까지(limit=4, leftDays=7)")
    @GetMapping("/deadline")
    public ResponseEntity<ApiResponse<PostTotalPagingResponse>> deadlineApproachingPosts(
            @RequestParam(defaultValue = "1") int page
//            @RequestParam(defaultValue = "10") int limit // 무조건 4개
    ) {
        return ApiResponse.success(GET_DEADLINE_APPROACHING_POST_SUCCESS, supportService.deadlineApproachingPosts(page-1));
    }


}
