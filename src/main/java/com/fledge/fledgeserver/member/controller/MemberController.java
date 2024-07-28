package com.fledge.fledgeserver.member.controller;

import com.fledge.fledgeserver.member.dto.MemberNicknameUpdateRequest;
import com.fledge.fledgeserver.member.dto.MemberResponse;
import com.fledge.fledgeserver.member.service.MemberService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;

@Tag(name = "회원 관리 API", description = "회원 관리와 관련된 API")
@RestController
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @Operation(summary = "회원 상세 정보 조회", description = "회원 ID로 회원의 상세 정보를 조회합니다.")
    @GetMapping("/{id}")
    public ResponseEntity<MemberResponse> getMemberDetails(
            @Parameter(description = "회원 ID", required = true, example = "1") @PathVariable Long id) {
        MemberResponse memberResponse = memberService.getMemberDetails(id);
        return ResponseEntity.ok(memberResponse);
    }

    @Operation(summary = "회원 닉네임 수정", description = "회원 ID로 회원의 닉네임을 수정합니다.")
    @PutMapping("/{id}/nickname")
    public ResponseEntity<Void> updateNickname(
            @Parameter(description = "회원 ID", required = true, example = "1") @PathVariable Long id,
            @Parameter(description = "닉네임 수정 요청", required = true) @RequestBody MemberNicknameUpdateRequest request) {
        memberService.updateNickname(id, request.getNickname());
        return ResponseEntity.ok().build();
    }
}
