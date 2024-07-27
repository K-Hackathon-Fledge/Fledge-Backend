package com.fledge.fledgeserver.auth.dto;

import java.util.Map;

import com.fledge.fledgeserver.exception.AuthException;
import com.fledge.fledgeserver.member.entity.Member;
import com.fledge.fledgeserver.member.entity.Role;
import lombok.Builder;

import static com.fledge.fledgeserver.exception.ErrorCode.ILLEGAL_REGISTRATION_ID;

@Builder
public record OAuth2UserInfo(
        String nickname,
        String registerType,
        String email,
        String profile,
        Long socialId
) {

    public static OAuth2UserInfo of(String registrationId, Map<String, Object> attributes) {
        return switch (registrationId) {
            case "kakao" -> ofKakao(attributes);
            default -> throw new AuthException(ILLEGAL_REGISTRATION_ID);
        };
    }

    private static OAuth2UserInfo ofKakao(Map<String, Object> attributes) {

        Map<String, Object> account = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) account.get("profile");


        return OAuth2UserInfo.builder()
                .socialId((Long) attributes.get("id"))
                .nickname((String) profile.get("nickname"))
                .email((String) account.get("email"))
                .profile((String) profile.get("profile_image_url"))
                .registerType("KAKAO")
                .build();
    }

    public Member toEntity() {
        return Member.builder()
                .socialId(socialId)
                .nickname(nickname)
                .email(email)
                .profile(profile)
                .role(Role.USER)
                .registerType("KAKAO")
                .build();
    }
}
