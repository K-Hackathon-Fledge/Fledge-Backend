package com.fledge.fledgeserver.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public enum SuccessStatus {

    /**
     * member
     */
    MEMBER_INFO_RETRIEVAL_SUCCESS(HttpStatus.OK, "회원 정보 조회 성공"),
    MEMBER_DETAILS_RETRIEVAL_SUCCESS(HttpStatus.OK, "회원 상세 정보 조회 성공"),
    MEMBER_NICKNAME_UPDATE_SUCCESS(HttpStatus.OK, "회원 닉네임 수정 성공"),

    /**
     * support
     */
    CREATE_SUPPORT_SUCCESS(HttpStatus.CREATED, "후원하기 게시글 등록 성공"),
    GET_SUPPORT_SUCCESS(HttpStatus.OK, "후원하기 상세 페이지 조회 성공"),

    /**
     * canary
     */
    PROFILE_APPLICATION_SUCCESS(HttpStatus.OK, "프로필 신청 성공"),
    PROFILE_RETRIEVAL_SUCCESS(HttpStatus.OK, "프로필 조회 성공"),
    PROFILE_UPDATE_SUCCESS(HttpStatus.NO_CONTENT, "프로필 수정 성공"),
    DELIVERY_INFO_GET_SUCCESS(HttpStatus.OK, "배송지 조회 성공");


    private final HttpStatus httpStatus;
    private final String message;

    public int getStatusCode() {
        return this.httpStatus.value();
    }
}

