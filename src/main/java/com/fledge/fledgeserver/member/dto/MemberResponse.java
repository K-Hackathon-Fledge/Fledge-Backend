package com.fledge.fledgeserver.member.dto;

import com.fledge.fledgeserver.member.entity.Member;
import com.fledge.fledgeserver.member.entity.Role;
import lombok.Getter;

@Getter
public class MemberResponse {
    private Long id;
    private String nickname;
    private String email;
    private String profile;
    private Role role;
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
