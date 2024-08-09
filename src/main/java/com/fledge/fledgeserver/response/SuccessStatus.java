package com.fledge.fledgeserver.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public enum SuccessStatus {

    /**
     * auth
     */
    TOKEN_REFRESH_SUCCESS(HttpStatus.OK, "토큰 갱신 성공"),
    LOGOUT_SUCCESS(HttpStatus.OK, "로그아웃 성공"),

    /**
     * member
     */
    MEMBER_INFO_RETRIEVAL_SUCCESS(HttpStatus.OK, "회원 정보 조회 성공"),
    MEMBER_DETAILS_RETRIEVAL_SUCCESS(HttpStatus.OK, "회원 상세 정보 조회 성공"),
    MEMBER_NICKNAME_UPDATE_SUCCESS(HttpStatus.OK, "회원 닉네임 수정 성공"),

    /**
     * file
     */
    FILE_RETRIEVAL_SUCCESS(HttpStatus.OK, "회원 정보 조회 성공"),

    /**
     * support
     */
    CREATE_SUPPORT_SUCCESS(HttpStatus.CREATED, "후원하기 게시글 등록 성공"),
    GET_SUPPORT_SUCCESS(HttpStatus.OK, "후원하기 상세 페이지 조회 성공"),
    GET_SUPPORT_FOR_UPDATE_SUCCESS(HttpStatus.OK, "수정을 위한 후원하기 조회 성공"),
    UPDATE_SUPPORT_SUCCESS(HttpStatus.OK, "후원하기 게시글 업데이트 성공"),
    CREATE_DONATE_SUCCESS(HttpStatus.CREATED, "후원하기 성공"),
    GET_SUPPORT_POST_PAGING_SUCCESS(HttpStatus.OK, "후원하기 게시글 페이징 조회 성공"),
    GET_DEADLINE_APPROACHING_POST_SUCCESS(HttpStatus.OK, "마감 임박 후원하기 게시글 조회 성공"),
    GET_SUPPORT_PROGRESS_SUCCESS(HttpStatus.OK, "후원하기 진행률 조회 성공"),
    DELETE_SUPPORT_SUCCESS(HttpStatus.OK, "후원하기 게시글 삭제 성공"),

    /**
     * canary
     */
    PROFILE_APPLICATION_SUCCESS(HttpStatus.OK, "프로필 신청 성공"),
    PROFILE_RETRIEVAL_SUCCESS(HttpStatus.OK, "프로필 조회 성공"),
    PROFILE_UPDATE_SUCCESS(HttpStatus.NO_CONTENT, "프로필 수정 성공"),
    DELIVERY_INFO_GET_SUCCESS(HttpStatus.OK, "배송지 조회 성공"),

    /**
     * challenge
     */
    CHALLENGE_RETRIEVAL_SUCCESS(HttpStatus.OK, "챌린지 조회 성공"),
    CHALLENGE_PARTICIPATION_SUCCESS(HttpStatus.OK, "챌린지 참여 성공"),
    CHALLENGE_PROOF_UPLOAD_SUCCESS(HttpStatus.OK, "챌린지 인증 업로드 성공"),
    CHALLENGE_UPDATE_SUCCESS(HttpStatus.NO_CONTENT, "챌린지 업데이트 성공"),
    CHALLENGE_PARTICIPANTS_RETRIEVED_SUCCESS(HttpStatus.OK, "챌린지 참여자 목록 조회에 성공했습니다."),
    CHALLENGE_PROOFS_RETRIEVED_SUCCESS(HttpStatus.OK, "챌린지 인증 내역 조회에 성공했습니다."),
    CHALLENGE_DETAILS_RETRIEVED_SUCCESS(HttpStatus.OK, "챌린지 상세 조회에 성공했습니다.");


    private final HttpStatus httpStatus;
    private final String message;

    public int getStatusCode() {
        return this.httpStatus.value();
    }
}

