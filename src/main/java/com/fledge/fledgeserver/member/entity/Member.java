package com.fledge.fledgeserver.member.entity;

import com.fledge.fledgeserver.common.entity.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private Long socialId;

    @Column(nullable = false, length = 20)
    private String nickname;

    @Column(nullable = false, length = 255)
    private String email;

    @Column(nullable = false)
    private String profile;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Role role;

    @Column(nullable = false)
    private String registerType;


    @Builder
    public Member(Long socialId, String nickname, String email, String profile, Role role, String registerType) {
        this.socialId = socialId;
        this.nickname = nickname;
        this.email = email;
        this.profile = profile;
        this.role = role;
        this.registerType = registerType;
    }

}

