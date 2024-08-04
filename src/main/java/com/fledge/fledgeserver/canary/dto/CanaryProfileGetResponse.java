package com.fledge.fledgeserver.canary.dto;

import com.fledge.fledgeserver.canary.entity.CanaryProfile;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(description = "후원하기에서 자립준비청년 프로필 조회 응답 DTO")
public class CanaryProfileGetResponse {

    @Schema(description = "닉네임", example = "카드값줘체리")
    private String nickname;

    @Schema(description = "자기 소개", example = "안녕하세요, 저는...")
    private String introduction;


    public CanaryProfileGetResponse(CanaryProfile canaryProfile) {
        this.nickname = canaryProfile.getMember().getNickname();
        this.introduction = canaryProfile.getIntroduction();
    }
}
