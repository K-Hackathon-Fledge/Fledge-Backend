package com.fledge.fledgeserver.canary.dto;

import com.fledge.fledgeserver.canary.entity.CanaryProfile;
import com.fledge.fledgeserver.member.entity.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.Date;

@Getter
@Schema(description = "후원하기에서 자립준비청년 프로필 조회 응답 DTO")
public class CanaryProfileGetResponseDto {

    @Schema(description = "닉네임", example = "카드값줘체리")
    private String nickname;

    @Schema(description = "자기 소개", example = "안녕하세요, 저는...")
    private String introduction;


    public CanaryProfileGetResponseDto(CanaryProfile canaryProfile) {
        this.nickname = canaryProfile.getMember().getNickname();
        this.introduction = canaryProfile.getIntroduction();
    }
}
