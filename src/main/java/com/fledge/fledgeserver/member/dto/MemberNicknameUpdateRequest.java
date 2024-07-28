package com.fledge.fledgeserver.member.dto;

import lombok.Getter;
import lombok.Setter;

import io.swagger.v3.oas.annotations.media.Schema;

@Getter
@Setter
@Schema(description = "회원 닉네임 수정 요청 DTO")
public class MemberNicknameUpdateRequest {

    @Schema(description = "새 닉네임", example = "new_nickname", required = true)
    private String nickname;
}
