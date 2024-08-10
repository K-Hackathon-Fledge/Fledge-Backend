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
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.SQLDelete;
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
            description = "후원하기 게시글을 등록합니다.(자립 준비 청소년만)\n" +
                    "\n" +
                    "### promise\n" +
                    "[ONCE, WEEKLY, MONTHLY]" +
                    "\n" +
                    "### supportCategory\n" +
                    "[DAILY_NECESSITY, FOOD, HOME_APPLIANCES, EDUCATION, MEDICAL, LEGAL_AID, ETC]" +
                    "\n" +
                    "### 필수 정보\n" +
                    "- **MEDICAL** 또는 **LEGAL_AID** 카테고리를 선택할 경우, (은행 정보와 계좌 정보)가 필수입니다.\n" +
                    "- 기타 카테고리 선택 시 (이름, 전화번호, 주소, 상세 주소, 우편번호)가 필요합니다.")
    @PostMapping
    public ResponseEntity<ApiResponse<Object>> createSupport(
            Principal principal,
            @Valid @RequestBody PostCreateRequest postCreateRequest
    ) {
        Long memberId = SecurityUtils.getCurrentUserId(principal);
        supportService.createSupport(memberId, postCreateRequest);
        return ApiResponse.success(CREATE_SUPPORT_SUCCESS);
    }

    @Operation(summary = "후원 물품 금액 후원하기",
            description = "후원하기 게시글에서 후원 물품 금액 조회합니다.(모든 회원 가능)\n" +
                    "\n" +
                    "후원 시 후원 물품 금액을 초과하면 후원 물품 금액 초과 예외처리(400)")
    @PostMapping("/{supportId}/record")
    public ResponseEntity<ApiResponse<Object>> createSupportRecord(
            @PathVariable(value = "supportId") Long supportId,
            @Valid @RequestBody RecordCreateRequest donationRequestDto,
            Principal principal
    ) {
        Long memberId = SecurityUtils.getCurrentUserId(principal);
        supportService.createSupportRecord(supportId, donationRequestDto, memberId);
        return ApiResponse.success(CREATE_DONATE_SUCCESS);
    }

    @Operation(summary = "후원하기 게시글 수정 시 기존 데이터 조회",
            description = "후원하기 게시글의 기존 데이터를 반환합니다.\n" +
                    "\n" +
                    "### 요청 정보\n" +
                    "- **supportId**: 수정할 게시글의 ID\n" +
                    "\n" +
                    "### 상태 정보\n" +
                    "- **Status가 PENDING**인 경우: 공통 필드 수정 가능\n" +
                    "- **그 외 상태**인 경우: 수정 불가")
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

    @Operation(summary = "후원하기 게시글 수정",
            description = "후원하기 게시글을 수정합니다.")
    @PutMapping("/{supportId}")
    public ResponseEntity<ApiResponse<PostGetResponse>> updateSupportPost(
            Principal principal,
            @PathVariable(value = "supportId") Long supportId,
            @Valid @RequestBody PostUpdateRequest postUpdateRequest
    ) {
        Long memberId = SecurityUtils.getCurrentUserId(principal);
        supportService.updateSupportPost(memberId, supportId, postUpdateRequest);
        return ApiResponse.success(SuccessStatus.UPDATE_SUPPORT_SUCCESS);
    }

    @Operation(summary = "후원하기 게시글 삭제", description = "후원하기 게시글을 삭제합니다.")
    @DeleteMapping("/{supportId}")
    public ResponseEntity<ApiResponse<Object>> deleteSupportPost(
            Principal principal,
            @PathVariable(value = "supportId") Long supportId
    ) {
        Long memberId = SecurityUtils.getCurrentUserId(principal);
        supportService.deleteSupportPost(memberId, supportId);
        return ApiResponse.success(SuccessStatus.DELETE_SUPPORT_SUCCESS);
    }
}
