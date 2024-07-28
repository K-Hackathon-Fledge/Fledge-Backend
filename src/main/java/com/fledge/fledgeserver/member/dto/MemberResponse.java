package com.fledge.fledgeserver.member.dto;

import com.fledge.fledgeserver.member.entity.Member;
import com.fledge.fledgeserver.member.entity.Role;
import lombok.Getter;

import io.swagger.v3.oas.annotations.media.Schema;

@Getter
@Schema(description = "회원 응답 DTO")
public class MemberResponse {
    @Schema(description = "회원 ID", example = "1")
    private Long id;

    @Schema(description = "회원 닉네임", example = "nickname")
    private String nickname;

    @Schema(description = "회원 이메일", example = "email@example.com")
    private String email;

    @Schema(description = "프로필 url", example = "http://t1.kakaocdn.net/account_images/default_profile.jpeg.twg.thumb.R640x640")
    private String profile;

    @Schema(description = "회원 역할", example = "USER")
    private Role role;

    @Schema(description = "소셜 로그인 유형", example = "KAKAO")
    private String registerType;

    public MemberResponse(Member member) {
        this.id = member.getId();
        this.nickname = member.getNickname();
        this.email = member.getEmail();
        this.profile = member.getProfile();
        this.role = member.getRole();
        this.registerType = member.getRegisterType();
    }
}
