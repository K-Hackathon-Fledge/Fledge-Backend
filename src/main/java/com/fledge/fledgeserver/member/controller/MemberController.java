package com.fledge.fledgeserver.member.controller;

import com.fledge.fledgeserver.member.dto.MemberNicknameUpdateRequest;
import com.fledge.fledgeserver.member.dto.MemberResponse;
import com.fledge.fledgeserver.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/{id}")
    public ResponseEntity<MemberResponse> getMemberDetails(@PathVariable Long id) {
        MemberResponse memberResponse = memberService.getMemberDetails(id);
        return ResponseEntity.ok(memberResponse);
    }

    @PutMapping("/{id}/nickname")
    public ResponseEntity<Void> updateNickname(@PathVariable Long id, @RequestBody MemberNicknameUpdateRequest request) {
        memberService.updateNickname(id, request.getNickname());
        return ResponseEntity.ok().build();
    }
}
