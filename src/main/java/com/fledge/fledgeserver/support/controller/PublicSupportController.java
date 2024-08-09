package com.fledge.fledgeserver.support.controller;

import com.fledge.fledgeserver.response.ApiResponse;
import com.fledge.fledgeserver.support.dto.response.PostGetResponse;
import com.fledge.fledgeserver.support.dto.response.PostPagingResponse;
import com.fledge.fledgeserver.support.dto.response.PostTotalPagingResponse;
import com.fledge.fledgeserver.support.dto.response.RecordProgressGetResponse;
import com.fledge.fledgeserver.support.service.SupportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.fledge.fledgeserver.response.SuccessStatus.*;
import static com.fledge.fledgeserver.response.SuccessStatus.GET_DEADLINE_APPROACHING_POST_SUCCESS;

@Tag(name = "후원하기 관련 API (Public)", description = "후원하기와 관련된 API (Public)")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/public/supports")
public class PublicSupportController {
    private final SupportService supportService;

    @Operation(summary = "후원하기 게시글 조회",
            description = "후원하기 게시글을 조회합니다.(모든 회원 가능)")
    @GetMapping("/{supportId}")
    public ResponseEntity<ApiResponse<PostGetResponse>> getSupport(
            @PathVariable(value = "supportId") Long supportId
    ) {
        // TODO :: 후원 인증 관련 로직 추가
        return ApiResponse.success(GET_SUPPORT_SUCCESS, supportService.getSupport(supportId));
    }

    @Operation(summary = "후원 진행률",
            description = "후원하기 게시글 및 후원하기 시에 후원 진행률 반환")
    @GetMapping("/{supportId}/progress")
    public ResponseEntity<ApiResponse<RecordProgressGetResponse>> getSupportProgress(
            @PathVariable(value = "supportId") Long supportId
    ) {
        return ApiResponse.success(GET_SUPPORT_PROGRESS_SUCCESS, supportService.getSupportProgress(supportId));
    }

    @Operation(summary = "후원하기 게시글 리스트 페이징",
            description = "후원하기 게시글을 페이징하여 조회합니다.\n" +
                    "\n" +
                    "### 요청 파라미터\n" +
                    "- **page**: 현재 페이지 번호 (기본값: 1)\n" +
                    "- **q**: 검색어 (제목, 내용 기준으로 검색)\n" +
                    "- **category**: 카테고리 (여러 개 선택 가능)\n" +
                    "  - 사용 예: category=\"Food\"&category=\"MEDICAL\"\n" +
                    "- **status**: 게시글 상태 ('ing'/'end', 기본값: 'ing')\n" +
                    "\n" +
                    "### 가능한 카테고리\n" +
                    "[DAILY_NECESSITY, FOOD, HOME_APPLIANCES, EDUCATION, MEDICAL, LEGAL_AID, ETC]")
    @GetMapping("/paging")
    public ResponseEntity<ApiResponse<PostTotalPagingResponse>> pagingSupportPost(
            @RequestParam(defaultValue = "1") int page, // 현재 페이지
//            @RequestParam(defaultValue = "9") int limit //무조건 9개
            @RequestParam(defaultValue = "") String q,
            @RequestParam(defaultValue = "") List<String> category, // 카테고리
            @RequestParam(defaultValue = "ing") String status
    ) {
        // 응답에 이미지 포함 시키기
        return ApiResponse.success(GET_SUPPORT_POST_PAGING_SUCCESS, supportService.pagingSupportPost(page-1, q, category, status));
    }

    @Operation(summary = "마감 임박한 후원하기 게시글",
            description = "4개씩 D-Day부터 D-7까지 한번에 리스트로 반환합니다.")
    @GetMapping("/deadline-approaching")
    public ResponseEntity<ApiResponse<List<PostPagingResponse>>> deadlineApproachingPosts() {
        return ApiResponse.success(GET_DEADLINE_APPROACHING_POST_SUCCESS, supportService.deadlineApproachingPosts());
    }
}
