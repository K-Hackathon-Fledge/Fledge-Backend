package com.fledge.fledgeserver.support.controller;

import com.fledge.fledgeserver.common.utils.SecurityUtils;
import com.fledge.fledgeserver.response.ApiResponse;
import com.fledge.fledgeserver.support.dto.request.SupportRecordCreateRequest;
import com.fledge.fledgeserver.support.dto.request.SupportPostCreateRequest;
import com.fledge.fledgeserver.support.dto.response.SupportPostGetResponse;
import com.fledge.fledgeserver.support.dto.response.SupportRecordProgressGetResponse;
import com.fledge.fledgeserver.support.service.SupportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

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
            @RequestBody SupportPostCreateRequest supportPostCreateRequest
    ) {
        Long memberId = SecurityUtils.getCurrentUserId(principal);
        supportService.createSupport(memberId, supportPostCreateRequest);
        return ApiResponse.success(CREATE_SUPPORT_SUCCESS);
    }

    @Operation(summary = "후원하기 게시글 조회",
            description = "후원하기 게시글을 조회합니다.(모든 회원 가능)")
    @GetMapping("/{supportId}")
    public ResponseEntity<ApiResponse<SupportPostGetResponse>> getSupport(
            @PathVariable(value = "supportId") Long supportId
    ) {
        // TODO :: 후원 인증 관련 로직 추가
        return ApiResponse.success(GET_SUPPORT_SUCCESS, supportService.getSupport(supportId));
    }
    // TODO :: 후원 게시글 둘러보기를 후원하기 상세 페이지에서도 봐얗마

    @Operation(summary = "후원 물품 금액 후원하기",
            description = "후원하기 게시글에서 후원 물품 금액 조회합니다.(모든 회원 가능)")
    @PostMapping("/{supportId}/record")
    public ResponseEntity<ApiResponse<Object>> createSupportRecord(
            @PathVariable(value = "supportId") Long supportId,
            @RequestBody SupportRecordCreateRequest donationRequestDto,
            Principal principal
    ) {
        Long memberId = SecurityUtils.getCurrentUserId(principal);
        // 후원 로직 처리
        supportService.createSupportRecord(supportId, donationRequestDto, memberId);
        return ApiResponse.success(CREATE_DONATE_SUCCESS);
    }

    @Operation(summary = "후원 진행률",
            description = "후원하기 게시글 및 후원하기 시에 후원 진행률 반환")
    @GetMapping("/{supportId}/progress")
    public ResponseEntity<ApiResponse<SupportRecordProgressGetResponse>> getSupportProgress(
            @PathVariable(value = "supportId") Long supportId
    ) {
        return ApiResponse.success(GET_SUPPORT_PROGRESS_SUCCESS, supportService.getSupportProgress(supportId));
    }

//
//    /**
//     * UPDATE
//     */
//
//    @Operation(summary = "후원하기 게시글 수정 시 기존 데이터 조회", description = "후원하기 게시글의 기존 데이터를 반환합니다.")
//    @GetMapping("/{supportId}/update")
//    public ResponseEntity<ApiResponse<SupportGetForUpdateResponse>> getSupportForUpdate(
//            @PathVariable(value = "supportId") Long supportId,
//            Principal principal
//    ) {
//        Long memberId = SecurityUtils.getCurrentUserId(principal);
//        return ApiResponse.success(
//                SuccessStatus.GET_SUPPORT_FOR_UPDATE_SUCCESS,
//                supportService.getSupportForUpdate(memberId, supportId)
//        );
//    }
//
//    @Operation(summary = "후원하기 게시글 수정", description = "후원하기 게시글을 수정합니다.")
//    @PutMapping("/{supportId}")
//    public ResponseEntity<ApiResponse<SupportPostGetResponse>> updateSupport(
//            Principal principal,
//            @PathVariable(value = "supportId") Long supportId,
//            @RequestBody SupportUpdateRequest supportUpdateRequestDto
//    ) {
//        Long memberId = SecurityUtils.getCurrentUserId(principal);
//        supportService.updateSupport(memberId, supportId, supportUpdateRequestDto);
//        return ApiResponse.success(SuccessStatus.UPDATE_SUPPORT_SUCCESS);
//    }

    /**
     * DELETE
     */
    // TODO :: 후원하기 게시글 삭제 API


    /**
     * ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ후원 하기ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
     */
    /**
     * 후원하기 팝업에서 정보 조회
     */


    /**
     * 후원하기 시 후원 처리
     */

}
