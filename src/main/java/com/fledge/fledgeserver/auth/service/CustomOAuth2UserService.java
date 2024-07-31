package com.fledge.fledgeserver.auth.service;

import java.util.Collections;
import java.util.Map;

import com.fledge.fledgeserver.auth.dto.OAuthUserImpl;
import com.fledge.fledgeserver.auth.dto.OAuthAttributes;
import com.fledge.fledgeserver.member.entity.Member;
import com.fledge.fledgeserver.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    @Transactional
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        Map<String, Object> oAuth2UserAttributes = super.loadUser(userRequest).getAttributes();
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
                .getUserInfoEndpoint().getUserNameAttributeName();

        OAuthAttributes oauthAttributes = OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2UserAttributes);
        Member member = getOrSave(oauthAttributes);

        return new OAuthUserImpl(Collections.singleton(new SimpleGrantedAuthority(member.getRole().getKey())),
                oAuth2UserAttributes, oauthAttributes.getNameAttributeKey(), member);
    }

    private Member getOrSave(OAuthAttributes oauth2Attributes) {
        Member member = memberRepository.findBySocialId(oauth2Attributes.getSocialId())
                .orElseGet(oauth2Attributes::toEntity);
        return memberRepository.save(member);
    }
}