package com.fledge.fledgeserver.auth.service;

import com.fledge.fledgeserver.auth.dto.TokenResponse;
import com.fledge.fledgeserver.auth.jwt.TokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final TokenProvider tokenProvider;
    private final RedisTemplate<String, String> redisTemplate;
    @Value("${jwt.refresh_expired-time}")
    long refreshExpired;

    public void logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            redisTemplate.delete(authentication.getName());

            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }
    }

    public TokenResponse refreshToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        TokenResponse jwt = tokenProvider.createToken(authentication);
        redisTemplate.opsForValue().set(
                authentication.getName(),
                jwt.getRefreshToken(),
                refreshExpired,
                TimeUnit.SECONDS
        );
        return jwt;
    }


}
