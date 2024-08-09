package com.fledge.fledgeserver.auth.controller;

import com.fledge.fledgeserver.auth.dto.TokenResponse;
import com.fledge.fledgeserver.auth.service.AuthService;
import com.fledge.fledgeserver.response.ApiResponse;
import com.fledge.fledgeserver.response.SuccessStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "인증 관련 API", description = "인증과 관련된 API")
@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "로그아웃", description = "현재 사용자를 로그아웃 합니다.")
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<String>> logout(HttpServletRequest request, HttpServletResponse response) {
        authService.logout(request, response);
        return ApiResponse.success(SuccessStatus.LOGOUT_SUCCESS);
    }

    @Operation(summary = "토큰 재발급", description = "만료된 JWT 토큰을 재발급 합니다.")
    @GetMapping(value = "/tokenRefresh", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<TokenResponse>> refresh() {
        TokenResponse tokenResponse = authService.refreshToken();
        return ApiResponse.success(SuccessStatus.TOKEN_REFRESH_SUCCESS, tokenResponse);
    }
}
