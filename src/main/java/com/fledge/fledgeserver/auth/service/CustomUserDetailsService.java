package com.fledge.fledgeserver.auth.service;

import com.fledge.fledgeserver.auth.dto.OAuthUserImpl;
import com.fledge.fledgeserver.member.entity.Member;
import com.fledge.fledgeserver.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findById(Long.valueOf(username))
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + username));

        return new OAuthUserImpl(
                Collections.singleton(new SimpleGrantedAuthority(member.getRole().name())),
                createAttributes(member),
                "email",
                member
        );
    }

    // TODO :: Method 분리
    private Map<String, Object> createAttributes(Member member) {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("socialId", member.getSocialId());
        attributes.put("nickname", member.getNickname());
        attributes.put("email", member.getEmail());
        attributes.put("profile", member.getProfile());
        attributes.put("registerType", member.getRegisterType());
        return attributes;
    }
}