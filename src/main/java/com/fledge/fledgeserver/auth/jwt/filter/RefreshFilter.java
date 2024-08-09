package com.fledge.fledgeserver.auth.jwt.filter;

import com.fledge.fledgeserver.auth.jwt.TokenProvider;
import com.fledge.fledgeserver.exception.CustomException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static com.fledge.fledgeserver.exception.ErrorCode.INVALID_TOKEN;

@Slf4j
@Component
@RequiredArgsConstructor
public class RefreshFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;
    private final RedisTemplate<String, String> redisTemplate;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {


        if (request.getRequestURI().equals("/api/v1/auth/tokenRefresh")) {

            String jwt = tokenProvider.resolveToken(request);

            if (jwt != null) {
                tokenProvider.validateToken(jwt);

                Authentication authentication = tokenProvider.resolveToken(jwt);
                String refreshToken = redisTemplate.opsForValue().get(authentication.getName());
                if (refreshToken == null) {
                    throw new CustomException(INVALID_TOKEN, "Refresh Token not found.");
                } else if (!refreshToken.equals(jwt)) {
                    throw new CustomException(INVALID_TOKEN, "Refresh Token doesn't match.");
                }
            }
        }

        chain.doFilter(request, response);

    }

}