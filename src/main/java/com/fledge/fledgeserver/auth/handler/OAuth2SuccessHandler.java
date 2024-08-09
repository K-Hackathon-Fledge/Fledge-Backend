package com.fledge.fledgeserver.auth.handler;

import com.fledge.fledgeserver.auth.dto.TokenResponse;
import com.fledge.fledgeserver.auth.jwt.TokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@RequiredArgsConstructor
@Component
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final TokenProvider tokenProvider;
    @Value("${front.oauth-redirect-url}")
    private String oauthRedirectUrl;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        TokenResponse tokenResponse = tokenProvider.createToken(authentication);

        String redirectUrl = UriComponentsBuilder.fromUriString(oauthRedirectUrl)
                .queryParam("accessToken", tokenResponse.getAccessToken())
                .queryParam("refreshToken", tokenResponse.getRefreshToken())
                .build().toUriString();

        response.sendRedirect(redirectUrl);
    }
}