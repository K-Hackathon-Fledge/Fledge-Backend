package com.fledge.fledgeserver.auth.dto;

import com.fledge.fledgeserver.member.entity.Member;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

import java.util.Collection;
import java.util.Map;

@Getter
public class OAuthUserImpl extends DefaultOAuth2User implements UserDetails {
    private final Member member;

    public OAuthUserImpl(Collection<? extends GrantedAuthority> authorities,
                         Map<String, Object> attributes,
                         String nameAttributeKey,
                         Member member) {
        super(authorities, attributes, nameAttributeKey);
        this.member = member;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return member.getId().toString();
    }

    @Override
    public boolean isAccountNonExpired() {
        return member.isActive();
    }

    @Override
    public boolean isAccountNonLocked() {
        return member.isActive();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return member.isActive();
    }

    public Member getMember() {
        return member;
    }

}