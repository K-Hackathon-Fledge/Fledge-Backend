package com.fledge.fledgeserver.auth.dto;

import com.fledge.fledgeserver.exception.AuthException;
import com.fledge.fledgeserver.member.entity.Member;
import com.fledge.fledgeserver.member.entity.Role;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;
import java.util.UUID;

import static com.fledge.fledgeserver.exception.ErrorCode.ILLEGAL_REGISTRATION_ID;

@Getter
public class OAuthAttributes {
    private final Map<String, Object> attributes;
    private final String nameAttributeKey;
    private final String nickname;
    private final String email;
    private final String profile;
    private final String registerType;
    private final Long socialId;

    @Builder
    public OAuthAttributes(Map<String, Object> attributes, String nameAttributeKey, String nickname, String email, String profile, String registerType, Long socialId) {
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.nickname = nickname;
        this.email = email;
        this.profile = profile;
        this.registerType = registerType;
        this.socialId = socialId;
    }

    public static OAuthAttributes of(String socialName, String userNameAttributeName, Map<String, Object> attributes) {

        if ("kakao".equals(socialName)) {
            return ofKakao(userNameAttributeName, attributes);
        }

        throw new AuthException(ILLEGAL_REGISTRATION_ID);
    }

    private static OAuthAttributes ofKakao(String userNameAttributeName, Map<String, Object> attributes) {
        Map<String, Object> account = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) account.get("profile");

        return OAuthAttributes.builder()
                .nameAttributeKey(userNameAttributeName)
                .attributes(attributes)
                .socialId(Long.valueOf(attributes.get("id").toString()))
                .nickname((String) profile.get("nickname"))
                .email((String) account.get("email"))
                .profile((String) profile.get("profile_image_url"))
                .registerType("KAKAO")
                .build();
    }

    public Member toEntity() {
        return Member.builder()
                .socialId(socialId)
                .nickname(generateRandomNickname())
                .email(email)
                .profile(profile)
                .role(Role.USER)
                .registerType("KAKAO")
                .build();
    }

    private static String generateRandomNickname() {
        return "User_" + UUID.randomUUID().toString().replace("-", "").substring(0, 8);
    }
}
