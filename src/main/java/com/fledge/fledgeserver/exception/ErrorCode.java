package com.fledge.fledgeserver.exception;

import static org.springframework.http.HttpStatus.*;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    // auth
    ILLEGAL_REGISTRATION_ID(NOT_ACCEPTABLE, "잘못된 Registration ID 입니다."),
    TOKEN_EXPIRED(UNAUTHORIZED, "토큰이 만료되었습니다."),
    INVALID_TOKEN(UNAUTHORIZED, "올바르지 않은 토큰입니다."),
    INVALID_JWT_SIGNATURE(UNAUTHORIZED, "잘못된 JWT 시그니처입니다."),

    // member
    MEMBER_NOT_FOUND(NOT_FOUND, "회원을 찾을 수 없습니다."),

    // global
    NO_ACCESS(FORBIDDEN, "접근 권한이 없습니다."),
    DATA_INTEGRITY_VIOLATION(NOT_FOUND, "제약 조건(예: 고유 제약, 외래 키 제약, NULL 제약 등)을 위반하였습니다."),
    INVALID_REQUEST(BAD_REQUEST, "올바르지 않은 요청입니다."),

    // server error
    INTERNAL_ERROR(INTERNAL_SERVER_ERROR, "예상치못한 에러가 발생했습니다."),

    // canary profile
    CANARY_NOT_FOUND(NOT_FOUND, "자립준비청년을 찾을 수 없습니다."),
    DUPLICATE_APPLICATION(HttpStatus.CONFLICT, "이미 신청된 유저입니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
